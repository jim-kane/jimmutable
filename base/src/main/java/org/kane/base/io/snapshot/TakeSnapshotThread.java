package org.kane.base.io.snapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kane.base.io.SmallDocumentWriter;
import org.kane.base.io.benchmark.AWSAPIKeys;
import org.kane.base.io.benchmark.TestObjectProductData;

import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

public class TakeSnapshotThread // extends OperationPool
{
	static public final long MAXIMUM_OBJECT_SIZE = 1024l*1024l; // 1 Megabye
	static public final int THREAD_POOL_SIZE = 100;
	
	// Request
	private S3ListRequest list_request;
	private String snapshot_bucket_name;
	private String snapshot_key_name;
	
	// Statistics
	volatile  int stats_objects_written_to_snapshot = 0;
	volatile long stats_bytes_written_to_snapshot = 0;
	
	
	/*
	public TakeSnapshotThread(S3ListRequest request, String snapshot_bucket_name, String snapshot_key_name)
	{
		this.list_request = request;
		this.snapshot_bucket_name = snapshot_bucket_name;
		this.snapshot_key_name = snapshot_key_name;
	}
	
	public void run()
	{
		File temporary_file = null;
		
		try
		{
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			
			long t1 = System.currentTimeMillis();
			
			// Setup the S3 client
			s3_client = new AmazonS3Client(AWSAPIKeys.getAWSCredentialsDev());
			s3_client.setRegion(Region.getRegion(list_request.getSimpleRegion()));
			
			// Setup the thread pool
			thread_pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			
			// Start the listing process
			ListRunnable list_thread = new ListRunnable(list_request, new ListListener());
			all_operations.add(list_thread);
			thread_pool.submit(list_thread);
			
			// Setup the I/O
			temporary_file = File.createTempFile("snapshot", "dat");
			snapshot_writer = new SmallDocumentWriter(new FileOutputStream(temporary_file));
			
			while(true)
			{
				sleep(500); 
				
				System.out.println(String.format("Creating snapshot: %,d objects written, %,d KB in %,d ms", stats_objects_written_to_snapshot, stats_bytes_written_to_snapshot/1024l, System.currentTimeMillis() - t1));
				
				if ( state != ThreadedOperationState.IN_PROGRESS ) break;
				if ( areAllSubOperationsFinishedWithoutErrors() ) break;
				
				if ( hasAnySubOperationEncounteredAnError() ) 
				{
					state = ThreadedOperationState.FINISHED_WITH_ERRORS;
					break;
				}
			}
			
			thread_pool.shutdown();
			snapshot_writer.close();
			
			System.out.println();
			if ( state == ThreadedOperationState.IN_PROGRESS )
			{
				System.out.println(String.format("Finished taking snapshot! %,d objects written, %,d KB in %,d ms", stats_objects_written_to_snapshot, stats_bytes_written_to_snapshot/1024l, System.currentTimeMillis() - t1));
			}
			else
			{
				System.out.println(String.format("Error taking snapshot! %,d objects written, %,d KB in %,d ms", stats_objects_written_to_snapshot, stats_bytes_written_to_snapshot/1024l, System.currentTimeMillis() - t1));
			}
			System.out.println();
			
			
			long t2 = System.currentTimeMillis();
			
			if ( state == ThreadedOperationState.IN_PROGRESS )
			{
				TransferManager manager = new TransferManager(AWSAPIKeys.getAWSCredentialsDev()); 
				
				PutObjectRequest request = new PutObjectRequest(snapshot_bucket_name, snapshot_key_name, temporary_file);
				Upload upload = manager.upload(request);
				
				while(true)
				{
					if ( upload.isDone() ) break;
					
					long bytes_uploaded = upload.getProgress().getBytesTransfered();
					
					System.out.println(String.format("Uploaded %,d kb in %,d ms", bytes_uploaded/1024l, System.currentTimeMillis()-t2));
					
					Thread.currentThread().sleep(500);
				}
				
				manager.shutdownNow();

				System.out.println();
				System.out.println(String.format("Finished Upload %,d kb in %,d ms", temporary_file.length()/1024l, System.currentTimeMillis()-t2));
				System.out.println(String.format("Snapshot finished! %,d ms", System.currentTimeMillis()-t1));
			}
			else
			{
				System.out.println("Not uploading, state = "+state);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			state = ThreadedOperationState.FINISHED_WITH_ERRORS;
		}
		finally 
		{
			if ( state == ThreadedOperationState.IN_PROGRESS ) state = ThreadedOperationState.FINISHED_NO_ERRORS;
			
			// Stop any sub-operations that might still be running
			stopAllSubOperations();
			
			if ( temporary_file != null && temporary_file.exists() ) 
				temporary_file.delete();
		}
	}
	
	private boolean hasAnySubOperationEncounteredAnError()
	{
		for ( ThreadedOperation operation : all_operations )
		{
			if ( operation.getSimpleState() == ThreadedOperationState.FINISHED_WITH_ERRORS )
				return true;
		}
		
		return false;
	}
	
	private boolean areAllSubOperationsFinishedWithoutErrors()
	{
		for ( ThreadedOperation operation : all_operations )
		{
			if ( operation.getSimpleState() != ThreadedOperationState.FINISHED_NO_ERRORS )
				return false;
		}
		
		return true;
	}
	
	private class ListListener implements ListRunnable.Listener
	{
		public void onListObject(S3ListRequest list_request, S3ObjectSummary summary)
		{
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			
			// Don't add snapshots to the snapshot
			if ( summary.getKey().toLowerCase().endsWith(".snapshot") ) return;
				
			DownloadSmallObjectRunnable task = new DownloadSmallObjectRunnable(s3_client, summary, new DownloadListener());
			
			all_operations.add(task);
			thread_pool.submit(task);
		}

		public void onFinished(S3ListRequest request, ThreadedOperationState state)
		{
			if ( state == ThreadedOperationState.FINISHED_WITH_ERRORS ) 
				stopAllSubOperations();
		}
	}
	
	private class DownloadListener implements DownloadSmallObjectRunnable.Listener
	{
		public DownloadListener() {}

		public void onGetObjectBytes(S3ObjectSummary object_summary, byte[] data)
		{
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			
			try
			{
				synchronized(snapshot_writer)
				{
					snapshot_writer.writeDocument(new String(data));
					
					stats_objects_written_to_snapshot++;
					stats_bytes_written_to_snapshot += data.length;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				state = ThreadedOperationState.FINISHED_WITH_ERRORS;
			}
		}

		public void onFinished(S3ObjectSummary object_summary, ThreadedOperationState state)
		{
			if ( state == ThreadedOperationState.FINISHED_WITH_ERRORS ) 
				stopAllSubOperations();
		}
	}

	public void stopAllSubOperations()
	{
		for ( ThreadedOperation operation : all_operations )
			operation.ifInProgressStopOperation();
	}
	
	public ThreadedOperationState getSimpleState()
	{
		return state;
	}

	public void ifInProgressStopOperation()
	{
		if ( state == ThreadedOperationState.IN_PROGRESS ) 
			state = ThreadedOperationState.FINISHED_STOPPED;
	}*/
	
	
}
