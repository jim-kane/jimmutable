package org.kane.base.serialization;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Performance extends TestCase
{
	static private class DrinkingGlassPlain
	{
		private String design_name;
		private String designer_name;
		
		private float size_in_ounces;
		private float weight_in_grams;
		private float capacity;
		
		public DrinkingGlassPlain(String design_name, String designer_name, float size_in_ounces, float weight_in_grams, float capacity)
		{
			if ( design_name == null ) throw new ValidationException("design_name can not be null");
			if ( designer_name == null ) throw new ValidationException("designer_name can not be null");
			
			if ( size_in_ounces < 0.0f ) throw new ValidationException("size_in_ounces must be greater than zero");
			if ( weight_in_grams < 0.0f ) throw new ValidationException("weight_in_grams must be greater than zero");
			if ( capacity < 0.0f ) throw new ValidationException("capacity must be greater than zero");
			
			this.design_name = design_name.toLowerCase();
			this.designer_name = designer_name.toUpperCase();
			this.size_in_ounces = size_in_ounces;
			this.weight_in_grams = weight_in_grams;
			this.capacity = capacity;
		}
	}
	
	static private class DrinkingGlassStandard extends StandardImmutableObject
	{
		private String design_name;
		private String designer_name;
		
		private float size_in_ounces;
		private float weight_in_grams;
		private float capacity;
		
		public DrinkingGlassStandard(String design_name, String designer_name, float size_in_ounces, float weight_in_grams, float capacity)
		{
			this.design_name = design_name;
			this.designer_name = designer_name;
			this.size_in_ounces = size_in_ounces;
			this.weight_in_grams = weight_in_grams;
			this.capacity = capacity;
			
			complete();
		}

		public int compareTo(Object o) 
		{
			// TODO Auto-generated method stub
			return 0;
		}

		
		public void normalize() 
		{
			design_name = design_name.toLowerCase();
			designer_name = designer_name.toUpperCase();
		}
		
		public void validate()
		{
			Validator.notNull(design_name);
			Validator.notNull(designer_name);
			
			Validator.min(size_in_ounces,0.0f);
			Validator.min(weight_in_grams,0.0f);
			Validator.min(capacity,0.0f);
		}
		
		public int hashCode() 
		{
			return design_name.hashCode();
		}

		
		public boolean equals(Object obj) 
		{
			return toString().equals(obj.toString());
		}
	}
	
	
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Performance( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Performance.class );
    }

    public void testApp()
    {
    	try
    	{
    	createPlain(2_000_000);
    	createStandard(2_000_000);
    	
    	long t1 = System.currentTimeMillis();
    	createPlain(2_000_000);
    	
    	long t2 = System.currentTimeMillis();
    	createStandard(2_000_000);
    
    	long t3 = System.currentTimeMillis();
    	
    	System.out.println(t2-t1);
    	System.out.println(t3-t2);
    	}
    	catch(Exception e) { e.printStackTrace(); }
    	
    }
    
    public void createPlain(int n)
    {
    	List<DrinkingGlassPlain> objs = new ArrayList(n+100);
    	
    	while(objs.size() < n)
    	{
    		objs.add(new DrinkingGlassPlain("DESIGN_"+objs.size(), "designer_"+objs.size(), objs.size()*0.7f, objs.size()*4.7f, objs.size()*1.2f));
    	}
    }
    
    public void createStandard(int n)
    {
    	List<DrinkingGlassStandard> objs = new ArrayList(n+100);
    	
    	while(objs.size() < n)
    	{
    		objs.add(new DrinkingGlassStandard("DESIGN_"+objs.size(), "designer_"+objs.size(), objs.size()*0.7f, objs.size()*4.7f, objs.size()*1.2f));
    	}
    }
}

