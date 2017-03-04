package org.kane.base.io.snapshot;

import org.kane.base.io.SmallDocumentWriter;
import org.kane.base.serialization.Validator;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

public class DownloadSmallObjectRunnable implements Runnable, ThreadedOperation
{
	private AmazonS3Client client;
	private S3ObjectSummary object_summary;
	private Listener listener;
	
	private volatile ThreadedOperationState state = ThreadedOperationState.IN_PROGRESS;
	
	public DownloadSmallObjectRunnable(AmazonS3Client client, S3ObjectSummary object_summary, Listener listener)
	{
		Validator.notNull(client);
		Validator.notNull(object_summary);
		Validator.notNull(listener);
		
		
		this.client = client;
		this.object_summary = object_summary;
		this.listener = listener;
	}
	
	public void run() 
	{
		try
		{
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			if ( object_summary.getSize() > TakeSnapshotThread.MAXIMUM_OBJECT_SIZE ) return; // don't add the object to the snapshot, it is too large (THIS IS NOT AN ERROR -- SILENTLY SKIP)
			
			GetObjectRequest request = new GetObjectRequest(object_summary.getBucketName(),object_summary.getKey());
			
			S3Object s3_obj = client.getObject(request);
			byte[] data = IOUtils.toByteArray(s3_obj.getObjectContent());
			
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			
			listener.onGetObjectBytes(object_summary, data);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			state = ThreadedOperationState.FINISHED_WITH_ERRORS;
		}
		finally
		{
			if ( state == ThreadedOperationState.IN_PROGRESS ) state = ThreadedOperationState.FINISHED_NO_ERRORS;
			listener.onFinished(object_summary, state);
		}
	}
	
	
	public interface Listener
	{
		public void onGetObjectBytes(S3ObjectSummary object_summary, byte data[]);
		public void onFinished(S3ObjectSummary object_summary, ThreadedOperationState state);
	}


	public ThreadedOperationState getSimpleState()
	{
		return state;
	}

	
	public void ifInProgressStopOperation()
	{
		if ( state == ThreadedOperationState.IN_PROGRESS ) 
			state = ThreadedOperationState.FINISHED_STOPPED;
	}
}
