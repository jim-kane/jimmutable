package org.kane.db_experiments;

import java.util.Random;

public class RandomData
{
	static public final char ALPHABET_ALPHA_NUMERIC[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	static public final char ALPHABET_ALPHA_NUMERIC_UPPER_CASE[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
	static public final char ALPHABET_DIGITS[] = "0123456789".toCharArray();
	static public final char ALPHABET_ALPHA[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	static public final char ALPHABET_ALPHA_UPPER_CASE[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	private Random r = new Random();
	
	
	public RandomData()
	{
		
	}
	
	public String randomStringOfLength(char alphabet[], int length)
	{
		StringBuilder ret = new StringBuilder();
		
		while(length > 0)
		{
			ret.append(alphabet[r.nextInt(alphabet.length)]);
			length--;
		}
		
		return ret.toString();
	}
	
	public int randomInt(int min, int max)
	{
		if ( max < min )
		{
			int tmp = min;
			min = max;
			max = tmp;
		}
		
		return min + r.nextInt(max-min);
	}
	
	public String randomStringOfLength(char alphabet[], int min_length, int max_length)
	{
		return randomStringOfLength(alphabet, randomInt(min_length,max_length));
	}
	
	static public void main(String args[])
	{
		RandomData r = new RandomData();
		
		for ( int i = 0; i < 20; i++ )
		{
			System.out.println(r.randomStringOfLength(ALPHABET_ALPHA_NUMERIC_UPPER_CASE, 5, 50));
		}
	}
	
}
