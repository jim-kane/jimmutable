package org.kane.base.serialization;

import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriter;
import org.kane.base.serialization.writer.ObjectWriterUtils;
import org.kane.base.serialization.writer.StandardWritable;

import com.google.common.base.Objects;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ObjectFieldReadWriteTest extends TestCase
{
	static public class ObjectBoundaryTest implements StandardWritable
	{
		static public TypeName TYPE_NAME = new TypeName("serialization_tests.object_boundary_test");
		
		static private FieldName FIELD_STRING_MY_STRING = new FieldName("my_string");
		
		public String my_string;
		
		public ObjectBoundaryTest() 
		{
		}
		
		public ObjectBoundaryTest(ObjectReader r) 
		{
			my_string = r.readString(FIELD_STRING_MY_STRING,null);
			assertEquals(null,r.readString(FIELD_STRING_MY_STRING, null)); // make sure the read also removed the object...
		}

		
		public TypeName getTypeName() 
		{
			return TYPE_NAME;
		}

		public void write(ObjectWriter writer) 
		{
			writer.writeString(FIELD_STRING_MY_STRING, my_string);
		}
		
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof ObjectBoundaryTest)) return false;
			
			ObjectBoundaryTest other = (ObjectBoundaryTest)obj;
			
			if ( !Objects.equal(my_string, other.my_string) ) return false;
			
			return true;
		}
	}
	
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ObjectFieldReadWriteTest( String testName )
    {
        super( testName );
        
        ObjectReader.registerType(ObjectBoundaryTest.TYPE_NAME, ObjectBoundaryTest.class);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ObjectFieldReadWriteTest.class );
    }
    
    public void testSomething()
    {
    	{
    		ObjectBoundaryTest obj = new ObjectBoundaryTest();
    		testObject(obj);
    	}
    	
    	{
    		ObjectBoundaryTest obj = new ObjectBoundaryTest();
    		
    		obj.my_string = "foo";
    		
    		testObject(obj);
    	}
    	
    	{
    		ObjectBoundaryTest obj = new ObjectBoundaryTest();
    		
    		obj.my_string = String.format("This is a base64 string because it has a %c", (char)0);
    		
    		testObject(obj);
    	}
    }
    
    private void testObject(Object obj)
    {
    	testObject(Format.XML,obj);
    	testObject(Format.XML_PRETTY_PRINT,obj);
    	testObject(Format.JSON,obj);
    	testObject(Format.JSON_PRETTY_PRINT,obj);;
    }
    
    private void testObject(Format format, Object obj)
    {
    	/*if ( obj instanceof Long )
    	{
    		System.out.println(String.format("%s: %d", format, obj));
    	}*/
    	String serialized_data = ObjectWriterUtils.writeObject(format, obj, null);
    	assert(serialized_data != null);
    	
    	System.out.println(serialized_data);
    	
    	/*if ( obj instanceof Long )
    	{
    		System.out.println(serialized_data);
    	}*/
    	
    	Object from_reader = ObjectReader.readDocument(serialized_data, null);
    	
    	assertEquals(obj,from_reader);
    }
}

