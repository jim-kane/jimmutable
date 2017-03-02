package org.kane.base.io.benchmark;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.io.GZIPUtils;
import org.kane.base.io.SmallDocumentReader;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.StringUtils;

public class S3Benchmark 
{
	static private final String BUCKET_NAME = "rws-dev-jimmutable-s3-benchmark-us-west-2";
	static private final String EXPERIMENT_NAME = "first";
	
	static public void uploadFilesToS3(File src) throws Exception
	{
		System.out.println("Uploading objects into s3");
		System.out.println(String.format("Source file: %s", src.getAbsolutePath()));
		System.out.println(String.format("Destination Bucket: %s, experiment name: %s", BUCKET_NAME, EXPERIMENT_NAME));
		System.out.println();
		
		
		AmazonS3Client client = new AmazonS3Client(AWSAPIKeys.getAWSCredentialsDev());
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		
		//upsertBucket(client,BUCKET_NAME);
		
		InputStream in_raw = GZIPUtils.gunzipStreamIfNeeded(new FileInputStream(src));
		Reader reader_raw = new BufferedReader(new InputStreamReader(in_raw,"UTF-8"));
		SmallDocumentReader reader = new SmallDocumentReader(reader_raw);
		
		long t1 = System.currentTimeMillis();
		int object_count = 0;
		
		TransferManager manager = new TransferManager(AWSAPIKeys.getAWSCredentialsDev()); 

		List<Upload> in_progress = new ArrayList();
		
		for ( int i = 0; i < 1_000; i++ )
		{
		
			reader.readNextDocument();
			TestObjectProductData data = (TestObjectProductData)StandardImmutableObject.fromXML(reader.getCurrentDocument(null));
			
			PutObjectRequest request = createRequest(data);
			
			Upload upload = manager.upload(request);
			in_progress.add(upload);
		}
		
		System.out.println(String.format("Requests Created! Created %,d PutObjectRequest(s) objects in %,d ms", in_progress.size(), System.currentTimeMillis()-t1));
		System.out.println();
		
		while(true)
		{
			int complete_count = getCompleteCount(in_progress);
			
			if ( complete_count >= in_progress.size() )
				break;
			
			System.out.println(String.format("Uploaded %,d objects in %,d ms", complete_count, System.currentTimeMillis()-t1));
			
			Thread.currentThread().sleep(500);
		}

		
		System.out.println();
		System.out.println(String.format("Finished! Uploaded %,d objects in %,d ms", object_count, System.currentTimeMillis()-t1));
	}
	
	static private int getCompleteCount(List<Upload> uploads)
	{
		int count = 0;
		
		for ( Upload upload : uploads )
		{
			if ( upload.isDone() )
				count++;
		}
		
		return count;
	}
	
	static private PutObjectRequest createRequest(TestObjectProductData data)
	{
		byte[] contentBytes = data.toXML().getBytes(StringUtils.UTF8);

        InputStream is = new ByteArrayInputStream(contentBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain");
        metadata.setContentLength(contentBytes.length);

        return new PutObjectRequest(BUCKET_NAME, getS3ObjectKey(data), is, metadata);
	}
	
	static private String getS3ObjectKey(TestObjectProductData data)
	{
		return String.format("%s/%s/%s.xml", EXPERIMENT_NAME, data.getSimpleBrandCode(), data.getSimplePN());
	}

	static private void upsertBucket(AmazonS3Client client, String bucket_name)
	{
		CreateBucketRequest request = new CreateBucketRequest(bucket_name);
		request.setRegion(Regions.US_WEST_2.getName());
		
		client.createBucket(request);
		
		System.out.println("Created: "+bucket_name);
		

		// Turn versioning on
		{
			BucketVersioningConfiguration bvc = new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED);
			client.setBucketVersioningConfiguration(new SetBucketVersioningConfigurationRequest(bucket_name, bvc));
			
			System.out.println("turned versioning on: "+bucket_name);
		}
	}
	
}
