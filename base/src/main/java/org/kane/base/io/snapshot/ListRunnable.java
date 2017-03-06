package org.kane.base.io.snapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kane.base.io.SmallDocumentWriter;
import org.kane.base.io.benchmark.AWSAPIKeys;
import org.kane.base.io.benchmark.TestObjectProductData;
import org.kane.base.serialization.Validator;
import org.kane.base.threading.OperationRunnable;
import org.kane.base.threading.OperationRunnable.Result;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ListRunnable extends OperationRunnable
{
	private SnapshotRequest request;
	private TakeSnapshotThread snapshot_operation;
	
	public ListRunnable(SnapshotRequest request, TakeSnapshotThread snapshot_operation)
	{
		Validator.notNull(request);
		Validator.notNull(snapshot_operation);
		
		this.request = request;
		this.snapshot_operation = snapshot_operation;
	}
	
	protected Result performOperation() throws Exception
	{
		if ( shouldStop() ) return Result.STOPPED;

		AmazonS3Client client = new AmazonS3Client(AWSAPIKeys.getAWSCredentialsDev());
		client.setRegion(Region.getRegion(request.getSimpleRegion()));

		ListObjectsV2Request req = new ListObjectsV2Request();
		req = req.withBucketName(request.getSimpleSourceBucketName());
		req = req.withMaxKeys(1000);

		if ( request.getSimpleSourceListPrefix().length() > 0 )
			req = req.withPrefix(request.getSimpleSourceListPrefix());

		

		int object_count = 0;


		while(true)
		{
			if ( shouldStop() ) return Result.STOPPED;

			ListObjectsV2Result result = client.listObjectsV2(req);

			for ( S3ObjectSummary summary : result.getObjectSummaries() ) 
			{
				if ( shouldStop() ) return Result.STOPPED;
				if ( object_count >= request.getSimpleMaximumObjectCount() ) return Result.SUCCESS;

				//pool.submitOperation(new DownloadSmallObjectRunnable(client, summary, snapshot_operation));
				object_count++;
			}
			
			if ( !result.isTruncated() ) return Result.SUCCESS;

			req.setContinuationToken(result.getNextContinuationToken());
		}
	}

}
