package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

import org.kane.base.immutability.StandardImmutableObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldStringStringHashMapTest extends TestCase
{
	static private class MyTestObj extends StandardImmutableObject
	{
		private FieldStringStringHashMap my_map = new FieldStringStringHashMap();
		
		public MyTestObj(Map<String,String> initial_values)
		{
			my_map.putAll(initial_values);
			
			complete();
		}

		
		public int compareTo(Object o) { return 0; }

		
		public void freeze()
		{
			my_map.freeze();
		}

		public void normalize()
		{
		}

		public void validate()
		{
		}

		
		public int hashCode()
		{
			return my_map.hashCode();
		}

		
		public boolean equals(Object obj)
		{
			if ( !(obj instanceof MyTestObj) ) return false;
			
			MyTestObj other = (MyTestObj)obj;
			
			return my_map.equals(other.my_map);
		}
		
		
	}
	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FieldStringStringHashMapTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FieldStringStringHashMapTest.class );
    }

    /**
     * Test that the custom converter in FieldStringStringHashMap works properly
     */
    public void testApp()
    {
    	Map data_one = new HashMap();
    	
    	data_one.put("foo", "bar");
    	data_one.put("baz", "quz");
    	
    	
    	Map data_two = new HashMap();
    	
    	data_two.put("foo", "1");
    	data_two.put("bar", "2");
    	
    	MyTestObj one = new MyTestObj(data_one);
    	MyTestObj two = new MyTestObj(data_two);
    	
    	assertFalse(one.equals(two));
    	
    	MyTestObj xml_one = (MyTestObj)one.deepClone();
    	MyTestObj xml_two = (MyTestObj)two.deepClone();
    	
    	assertEquals(one,xml_one);
    	assertEquals(two,xml_two);
    }
}
