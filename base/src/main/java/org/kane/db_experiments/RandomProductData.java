package org.kane.db_experiments;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldHashMap;

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
		
		public RandomProductData createRandomProductData(int num_features)
		{
			RandomProductData ret = new RandomProductData(this);
			
			ret.brand_code = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 4,12);
			ret.pn = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 8,17);
			
			ret.category_code = random.randomStringOfLength(RandomData.ALPHABET_ALPHA_UPPER_CASE, 16,32);
			
			for ( int i = 0; i < num_features; i++ )
			{
				ret.kv_data.put(random.randomStringOfLength(RandomData.ALPHABET_ALPHA_NUMERIC_UPPER_CASE, 10, 20), random.randomStringOfLength(RandomData.ALPHABET_ALPHA_NUMERIC, 35, 200));
			}
			
			ret.complete();
			
			return ret;
		}
	}
	

}
