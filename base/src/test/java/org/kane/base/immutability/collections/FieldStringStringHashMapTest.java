package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.reader.ReadAs;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.WriteAs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FieldStringStringHashMapTest extends TestCase
{
	static private class MyTestObj extends StandardImmutableObject
	{
		static private final TypeName TYPE_NAME = new TypeName("jimmutable.FieldStringStringHashMapTest.MyTestObj"); public TypeName getTypeName() { return TYPE_NAME; }
		static private final FieldName FIELD_MY_MAP = new FieldName("my_map");
		
		
		private FieldMap<String,String> my_map;
		
		public MyTestObj(Map<String,String> initial_values)
		{
			my_map = new FieldHashMap();
			my_map.putAll(initial_values);
			
			complete();
		}
		
		public MyTestObj(ReadTree t)
		{
			my_map = t.getMapOfObjects(FIELD_MY_MAP, new FieldHashMap(), ReadAs.READ_AS_STRING, ReadAs.READ_AS_STRING, ReadTree.OnError.SKIP);
		}

		public void write(ObjectWriter writer) 
		{
			writer.writeMap(FIELD_MY_MAP, my_map, WriteAs.NATURAL_PRIMATIVES, WriteAs.NATURAL_PRIMATIVES);	
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
