package org.kane.base.serialization;

import java.util.ArrayList;
import java.util.List;

import org.kane.base.serialization.reader.ObjectReader;
import org.kane.base.serialization.reader.ReadTree;
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
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ObjectFieldReadWriteTest( String testName )
    {
        super( testName );
        
        ObjectReader.registerType(StringFieldBoundaryTest.TYPE_NAME, StringFieldBoundaryTest.class);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ObjectFieldReadWriteTest.class );
    }
    
	static public class StringFieldBoundaryTest implements StandardWritable
	{
		static public TypeName TYPE_NAME = new TypeName("serialization_tests.StringFieldBoundaryTest");
		
		static private FieldName FIELD_STRING_MY_STRING = new FieldName("my_string");
		static private FieldName FIELD_STRING_MY_STRING_EXPLICIT = new FieldName("my_string_explicit");
		
		public String my_string;
		
		public StringFieldBoundaryTest() 
		{
		}
		
		public StringFieldBoundaryTest(ReadTree t) 
		{
			my_string = t.getString(FIELD_STRING_MY_STRING,null);
			
			String my_string_explicit = t.getString(FIELD_STRING_MY_STRING_EXPLICIT, null);
			assertEquals(my_string,my_string_explicit);
		}

		
		public TypeName getTypeName() { return TYPE_NAME; }

		public void write(ObjectWriter writer) 
		{
			writer.writeString(FIELD_STRING_MY_STRING, my_string);
			writer.writeObject(FIELD_STRING_MY_STRING_EXPLICIT, my_string);
		}
		
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof StringFieldBoundaryTest)) return false;
			
			StringFieldBoundaryTest other = (StringFieldBoundaryTest)obj;
			
			if ( !Objects.equal(my_string, other.my_string) ) return false;
			
			return true;
		}
	}
	
	static public class StringListFieldBoundaryTest implements StandardWritable
	{
		static public TypeName TYPE_NAME = new TypeName("serialization_tests.StringFieldBoundaryTest");
		
		static private FieldName FIELD_STRING_MY_STRING = new FieldName("my_string");
		static private FieldName FIELD_STRING_MY_STRING_EXPLICIT = new FieldName("my_string_explicit");
		
		public List<String> my_list = new ArrayList();
		
		public StringListFieldBoundaryTest() 
		{
		}
		
		public StringListFieldBoundaryTest(ReadTree t) 
		{
			
		}

		
		public TypeName getTypeName() { return TYPE_NAME; }

		public void write(ObjectWriter writer) 
		{
			writer.writeString(FIELD_STRING_MY_STRING, my_string);
			writer.writeObject(FIELD_STRING_MY_STRING_EXPLICIT, my_string);
		}
		
		public boolean equals(Object obj) 
		{
			if (!(obj instanceof StringFieldBoundaryTest)) return false;
			
			StringFieldBoundaryTest other = (StringFieldBoundaryTest)obj;
			
			if ( !Objects.equal(my_string, other.my_string) ) return false;
			
			return true;
		}
	}

    public void testStringField()
    {
    	testStringField(null);
    	
    	testStringField("Hello World");
    	testStringField("");
    	
    	
    	testStringField("Fisher & Paykel");
    	testStringField("{ foo : \"bar\" }");
    	
    	testStringField("{ foo : \"bar\" }");
    	
    	testStringField("<html></html>");
    	
    	testStringField("Hello There \u00a9");
    	
    	testStringField(String.format("Hello: %c", (char)0));
    	
    	// The acid string...
    	testStringField(PrimativeReadWriteTest.createNonBase64AcidString());
    	testStringField(PrimativeReadWriteTest.createAcidString());
    }
    
    private void testStringField(String value)
    {
    	testStringField(value,null);
    }
    
    private void testStringField(String value, Format print_diagnostics_in_format)
    {
    	StringFieldBoundaryTest obj = new StringFieldBoundaryTest();
		
		obj.my_string = value;
		
		testObject(obj, print_diagnostics_in_format);
    }
    
    private void testObject(Object obj, Format print_diagnostics_in_format)
    {
    	testObject(Format.XML_PRETTY_PRINT,obj, print_diagnostics_in_format);
    	testObject(Format.JSON_PRETTY_PRINT,obj, print_diagnostics_in_format);
    	
    	testObject(Format.XML,obj,print_diagnostics_in_format);
    	testObject(Format.JSON,obj,print_diagnostics_in_format);
    }
    
    private void testObject(Format format, Object obj, Format print_diagnostics_in_format)
    {
    	
    	String serialized_data = ObjectWriterUtils.writeObject(format, obj, null);
    	assert(serialized_data != null);
    	
    	if ( format == print_diagnostics_in_format )
    	{
    		System.out.println(serialized_data);
    	}
    	
    	Object from_reader = ObjectReader.readDocument(serialized_data, null);
    	
    	assertEquals(obj,from_reader);
    }
}

