package org.kane.base.io.snapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kane.base.io.benchmark.AWSAPIKeys;
import org.kane.base.io.benchmark.TestObjectProductData;
import org.kane.base.serialization.Validator;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ListRunnable implements ThreadedOperation, Runnable
{
	private S3ListRequest request;
	private Listener listener;
	
	private volatile ThreadedOperationState state = ThreadedOperationState.IN_PROGRESS;
	
	public ListRunnable(S3ListRequest request, Listener listener)
	{
		Validator.notNull(request);
		Validator.notNull(listener);
		
		this.request = request;
		this.listener = listener;
	}
	
	public void run()
	{
		try
		{
			if ( state != ThreadedOperationState.IN_PROGRESS ) return;
			
			AmazonS3Client client = new AmazonS3Client(AWSAPIKeys.getAWSCredentialsDev());
			client.setRegion(Region.getRegion(request.getSimpleRegion()));
			
			ListObjectsV2Request req = new ListObjectsV2Request();
			req = req.withBucketName(request.getSimpleBucketName());
			req = req.withMaxKeys(1000);
			
			if ( request.hasListPrefix() )
				req = req.withPrefix(request.getOptionalListPrefix(null));
			
			if ( request.hasStartAfter() )
				req = req.withStartAfter(request.getOptionalStartAfter(null));
			
			int object_count = 0;
			
			
			while(true)
			{
				if ( state != ThreadedOperationState.IN_PROGRESS ) return;
				
				ListObjectsV2Result result = client.listObjectsV2(req);
				
				for ( S3ObjectSummary summary : result.getObjectSummaries() ) 
				{
					if ( state != ThreadedOperationState.IN_PROGRESS ) return;
					if ( isAfterEnd(summary) ) return;
					if ( object_count >= request.getSimpleMaximumObjectCount() ) return;
					
					listener.onListObject(request,summary);
					object_count++;
	            }
				
				if ( state != ThreadedOperationState.IN_PROGRESS ) return;
				if ( !result.isTruncated() ) return;
				
				req.setContinuationToken(result.getNextContinuationToken());
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
			
			listener.onFinished(request, state);
		}
	}
	
	private boolean isAfterEnd(S3ObjectSummary obj)
	{
		if ( !request.hasEndAfter() ) return false;
		
		return request.getOptionalEndAfter("").compareTo(obj.getKey()) > 0;
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
	
	public interface Listener
	{
		public void onListObject(S3ListRequest request, S3ObjectSummary summary);
		public void onFinished(S3ListRequest request, ThreadedOperationState state);
	}

}
