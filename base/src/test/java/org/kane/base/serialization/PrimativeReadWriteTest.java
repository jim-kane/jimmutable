package org.kane.base.serialization;

import org.kane.base.serialization.reader.Parser;
import org.kane.base.serialization.reader.ReadTree;
import org.kane.base.serialization.writer.Format;
import org.kane.base.serialization.writer.ObjectWriterUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PrimativeReadWriteTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PrimativeReadWriteTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PrimativeReadWriteTest.class );
    }

    public void testStrings()
    {
    	//testStings(Format.JSON);
    	testStings(Format.XML);
    }
    
    public void testStings(Format format)
    {
    	testString(format, "Hello World");
    	testString(format, "");
    	
    	testString(format, "Fisher & Paykel");
    	
    	testString(format, "{ foo : \"bar\" }");
    	
    	testString(format, "<html></html>");
    	
    	testString(format, "Hello There \u00a9");
    	
    	// The acid string...
    	testString(format, createAcidString());
    }
    
    private String createAcidString()
    {
    	StringBuilder ret = new StringBuilder();
    	
    	for ( int i = 0; i < 10_000; i++ )
    	{
    		ret.append((char)i);
    	}
    	
    	return ret.toString();
    }
    
    private void testString(Format format, String str)
    {
    	String serialized_data = ObjectWriterUtils.writeObject(format, str, null);
    	assert(serialized_data != null);
    	
    	System.out.println(serialized_data);
    	
    	ReadTree t = Parser.parse(serialized_data, null);
    	
    	assert(t != null);
    	
    	assertEquals(t.readString(FieldName.FIELD_NAME_TYPE_HINT, null), TypeName.TYPE_NAME_STRING.getSimpleName());
    	assertEquals(t.readString(FieldName.FIELD_NAME_PRIMATIVE_VALUE, null),str);
    }
}