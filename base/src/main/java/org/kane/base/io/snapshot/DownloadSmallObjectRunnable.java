package org.kane.base.io.snapshot;

import org.kane.base.io.SmallDocumentWriter;
import org.kane.base.io.snapshot.OperationRunnable.Result;
import org.kane.base.serialization.Validator;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

public class DownloadSmallObjectRunnable extends OperationRunnable
{
	private AmazonS3Client client;
	private S3ObjectSummary object_summary;
	private SmallDocumentWriter writer;
	
	public DownloadSmallObjectRunnable(AmazonS3Client client, S3ObjectSummary object_summary, SmallDocumentWriter writer)
	{
		Validator.notNull(client);
		Validator.notNull(object_summary);
		Validator.notNull(writer);
		
		
		this.client = client;
		this.object_summary = object_summary;
		this.writer = writer;
	}
	
	protected Result performOperation() throws Exception
	{
		if ( shouldStop() ) 
			return Result.STOPPED;
		
		if ( object_summary.getSize() > TakeSnapshotThread.MAXIMUM_OBJECT_SIZE ) return Result.SUCCESS; // don't add the object to the snapshot, it is too large (THIS IS NOT AN ERROR -- SILENTLY SKIP)

		GetObjectRequest request = new GetObjectRequest(object_summary.getBucketName(),object_summary.getKey());

		S3Object s3_obj = client.getObject(request);
		
		if ( shouldStop() ) 
			return Result.STOPPED;

		byte[] data = IOUtils.toByteArray(s3_obj.getObjectContent());

		if ( shouldStop() ) 
			return Result.STOPPED;

		writer.writeDocument(new String(data));

		return Result.SUCCESS;
	}
}
