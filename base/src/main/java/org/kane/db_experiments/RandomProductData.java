package org.kane.db_experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.serialization.StandardObject;
import org.kane.io.SmallDocumentReader;
import org.kane.io.SmallDocumentWriter;
import org.kane.io.StandardObjectBulkLoader;

import com.google.common.collect.ComparisonChain;

public class RandomProductData extends StandardImmutableObject
{
	private String brand_code;
	private String pn;
	private String category_code;
	
	private FieldHashMap<String,String> kv_data = new FieldHashMap();
	
	public RandomProductData()
	{
		complete();
	}
	
	private RandomProductData(Builder builder)
	{
		
	}
	 
	public int compareTo(Object o)
	{
		if ( !(o instanceof RandomProductData) ) 
			return 0;
		
		RandomProductData other = (RandomProductData)o;
		
		return pn.compareTo(other.pn);
	}
	public void freeze()
	{
		kv_data.freeze();
	}

	public void normalize()
	{
	}

	public void validate()
	{
	}

	
	public int hashCode()
	{
		return pn.hashCode();
	}


	public boolean equals(Object obj)
	{
		return false;
	}

	static public class Builder
	{
		private RandomData random = new RandomData();
		
		public Builder()
		{
			
		}
		
		public RandomProductData createRandomProductData()
		{
			RandomProductData ret = new RandomProductData(this);
			
			ret.brand_code = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 4,12);
			ret.pn = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 8,17);
			
			ret.category_code = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 16,32);
			
			for ( int i = 0; i < random.randomInt(20, 200); i++ )
			{
				ret.kv_data.put(random.randomStringOfLength(RandomData.ALPHABET_ALPHA_NUMERIC_UPPER_CASE, 10, 20), random.randomStringOfLength(RandomData.ALPHABET_ALPHA_NUMERIC, 35, 200));
			}
			
			ret.complete();
			
			return ret;
		}
	}
	
	static public void main(String args[]) throws Exception
	{
		// Create/write
		if (false)
		{
			Builder builder = new Builder();

			long t1 = System.currentTimeMillis();

			List<RandomProductData> items = new ArrayList();

			for ( int i = 0; i < 100_000; i++ )
			{
				items.add(builder.createRandomProductData());

				if ( i % 1_000 == 0 )
					System.out.println(String.format("Building: %,d", i));
			}

			long t2 = System.currentTimeMillis();

			System.out.println();
			System.out.println(String.format("Build Time: %,d ms", (t2-t1)));
			System.out.println();


			//GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File("c:\\test.gz")));
			
			OutputStream raw = new FileOutputStream(new File("c:\\test.dat"));
			SmallDocumentWriter out = new SmallDocumentWriter(new OutputStreamWriter(raw));

			int write_count = 0;

			t1 = System.currentTimeMillis();

			for ( RandomProductData item : items )
			{
				write_count++;

				out.writeDocument(item.toXML());

				if ( write_count % 10_000 == 0 )
					System.out.println(String.format("Writing: %,d", write_count));

			}

			out.close();

			t2 = System.currentTimeMillis();

			System.out.println();
			System.out.println(String.format("Write Time: %,d ms", (t2-t1)));
			System.out.println();
		}
		
		
		// Read
		if ( true )
		{
			Reader reader_raw = new BufferedReader(new InputStreamReader(new FileInputStream("c:\\test.dat"), "UTF-8"));
			SmallDocumentReader reader = new SmallDocumentReader(reader_raw);
			
			MyListener listener = new MyListener();
			StandardObjectBulkLoader loader = new StandardObjectBulkLoader(listener,32);
			loader.addSource(reader);
			loader.doneAddingSources();
			
			long t1 = System.currentTimeMillis();
			int count = 0;
			
			
			while(true)
			{
				count++;
				
				if ( count % 10 == 0 )
				{
					System.out.println(String.format("loading objects: %,d loaded in %,d ms", listener.data.size(), System.currentTimeMillis()-t1));
				}
				
				try { Thread.currentThread().sleep(100); } catch(Exception e) { e.printStackTrace(); }
				if ( listener.done ) break;
			}
			
			long t2 = System.currentTimeMillis();
			
			
			System.out.println();
			System.out.println(String.format("loading finished: %,d loaded in %,d ms", listener.data.size(), System.currentTimeMillis()-t1));
		}
		
		sleepForever();
	}
	
	static private class MyListener implements StandardObjectBulkLoader.Listener
	{
		private boolean done = false;
		private Set<RandomProductData> data = Collections.newSetFromMap(new ConcurrentHashMap());
		
		public void onObjectLoaded(StandardObject obj)
		{
			data.add((RandomProductData)obj);
		}

		@Override
		public void onBulkLoaderFinished()
		{
			System.out.println("Done loading objects!");
			done = true;
		}
	}
	
	static public void sleepForever()
	{
		try
		{
			while(true) Thread.currentThread().sleep(1000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
