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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.io.GZIPUtils;
import org.kane.base.io.SmallDocumentReader;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

public class S3Benchmark 
{
	static private final String BUCKET_NAME = "rws-dev-jimmutable-s3-benchmark-us-west-2";
	static private final String EXPERIMENT_NAME = "first";
	
	static public void loadFilesFromS3(int max_objects) throws Exception
	{
		System.out.println("Loading objects from s3");
		System.out.println(String.format("Source Bucket: %s, experiment name: %s", BUCKET_NAME, EXPERIMENT_NAME));
		System.out.println();
		
		AmazonS3Client client = new AmazonS3Client(AWSAPIKeys.getAWSCredentialsDev());
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		
		ListObjectsV2Request req = new ListObjectsV2Request();
		req = req.withBucketName(BUCKET_NAME);
		req = req.withMaxKeys(1000);
		req = req.withPrefix(EXPERIMENT_NAME+"/");
		
		List<Future<TestObjectProductData>> in_progress = new ArrayList();
		
		
		long t1 = System.currentTimeMillis();
		
		ExecutorService pool = Executors.newFixedThreadPool(100);
		
		while(true)
		{
			System.out.println(String.format("Listing %,d objects in %,d ms", in_progress.size(), System.currentTimeMillis()-t1));
			
			ListObjectsV2Result result = client.listObjectsV2(req);
			
			File tmp_dir = File.createTempFile("foo", "xml").getParentFile();
			
			for (S3ObjectSummary summary : result.getObjectSummaries()) 
			{
				GetObjectRequest request = new GetObjectRequest(summary.getBucketName(),summary.getKey());
                
				in_progress.add(pool.submit(new DownloadObjectTask(client,request)));
                
                if ( in_progress.size() >= max_objects ) 
                	break;
            }
			
			if ( !result.isTruncated() || in_progress.size() >= max_objects )
				break;
			
			req.setContinuationToken(result.getNextContinuationToken());
		}
		
		System.out.println(String.format("List Complete! Listed %,d objects in %,d ms", in_progress.size(), System.currentTimeMillis()-t1));
		System.out.println();
	
		
		while(true)
		{
			int complete_count = getDownloadCompleteCount(in_progress);
			
			if ( complete_count >= in_progress.size() )
				break;
			
			System.out.println(String.format("Downloaded %,d objects in %,d ms", complete_count, System.currentTimeMillis()-t1));
			
			Thread.currentThread().sleep(500);
		}
		
		pool.shutdown();
		
		System.out.println();
		System.out.println(String.format("Finished! Downloaded %,d objects in %,d ms", in_progress.size(), System.currentTimeMillis()-t1));
		
		System.exit(0);
	}
	
	static private int getDownloadCompleteCount(List<Future<TestObjectProductData>> downloads)
	{
		int count = 0;
		
		for ( Future download : downloads )
		{
			if ( download.isDone() )
				count++;
		}
		
		return count;
	}
	
	static private final class DownloadObjectTask implements Callable<TestObjectProductData>
	{
		private GetObjectRequest request;
		private AmazonS3Client client;
		
		public DownloadObjectTask(AmazonS3Client client, GetObjectRequest request)
		{
			this.request = request;
			this.client = client;
		}
		
		public TestObjectProductData call() 
		{
			try
			{
				S3Object s3_obj = client.getObject(request);
				byte[] bytes = IOUtils.toByteArray(s3_obj.getObjectContent());
				
				TestObjectProductData ret = (TestObjectProductData)TestObjectProductData.fromXML(new String(bytes));
				return ret;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}
	
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
		
		TransferManager manager = new TransferManager(AWSAPIKeys.getAWSCredentialsDev()); 
		 
		

		List<Upload> in_progress = new ArrayList();
		
		for ( int i = 0; i < 100_000; i++ )
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
			int complete_count = getUploadCompleteCount(in_progress);
			
			if ( complete_count >= in_progress.size() )
				break;
			
			System.out.println(String.format("Uploaded %,d objects in %,d ms", complete_count, System.currentTimeMillis()-t1));
			
			Thread.currentThread().sleep(500);
		}
		
		manager.shutdownNow();

		
		System.out.println();
		System.out.println(String.format("Finished! Uploaded %,d objects in %,d ms", in_progress.size(), System.currentTimeMillis()-t1));
		
		System.exit(0);
	}
	
	static private int getUploadCompleteCount(List<Upload> uploads)
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
