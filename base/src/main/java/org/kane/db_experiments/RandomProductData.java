package org.kane.db_experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;
import org.kane.base.serialization.StandardObject;

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
		{
			Builder builder = new Builder();

			long t1 = System.currentTimeMillis();

			List<RandomProductData> items = new ArrayList();

			for ( int i = 0; i < 10_000; i++ )
			{
				items.add(builder.createRandomProductData());

				if ( i % 10_000 == 0 )
					System.out.println(String.format("Building: %,d", i));
			}

			long t2 = System.currentTimeMillis();

			System.out.println();
			System.out.println(String.format("Build Time: %,d ms", (t2-t1)));
			System.out.println();


			//GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(new File("c:\\test.gz")));
			
			OutputStream raw = new FileOutputStream(new File("c:\\test.dat"));
			ObjectOutputStream out = StandardObject.createNewXMLObjectOutputStream(raw);

			int write_count = 0;

			t1 = System.currentTimeMillis();

			for ( RandomProductData item : items )
			{
				write_count++;

				out.writeObject(item);

				if ( write_count % 100 == 0 )
					System.out.println(String.format("Writing: %,d", write_count));

			}

			out.close();

			t2 = System.currentTimeMillis();

			System.out.println();
			System.out.println(String.format("Write Time: %,d ms", (t2-t1)));
			System.out.println();
		}
		
		
		// Read
		{
			/*GZIPInputStream zip = new GZIPInputStream(new FileInputStream(new File("c:\\test.gz")));
			Reader reader = new BufferedReader(new InputStreamReader(zip, "UTF-8"));
			
			Object obj = StandardObject.fromXML(reader);
			
			System.out.println(obj.getClass());
			
			obj = StandardObject.fromXML(reader);
			
			System.out.println(obj.getClass());*/
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
