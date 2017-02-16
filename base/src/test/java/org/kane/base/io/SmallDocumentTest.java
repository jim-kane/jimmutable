package org.kane.base.io;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.kane.io.SmallDocumentReader;
import org.kane.io.SmallDocumentWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SmallDocumentTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SmallDocumentTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SmallDocumentTest.class );
    }

    public void testOnStaticDocuments()
    {
    	String documents = " <?a?>A<?a?> <?foo?>B<?foo?> <?z?>C<?z?> <?a?>--end-of-file--<?a?>";
    	
    	SmallDocumentReader r = new SmallDocumentReader(new StringReader(documents));
    	
    	assertEquals(r.readDocument(null),"A");
    	assertEquals(r.readDocument(null),"B");
    	assertEquals(r.readDocument(null),"C");

    	assertTrue(r.isEOFDocument(r.readDocument(null)));
    }
    
    public void testReadingOfContentPreparedByWriter()
    {
    	int size = 100;
    	
    	String documents = createNDocumentsUsingWriter(size);
    	

    	SmallDocumentReader r = new SmallDocumentReader(new StringReader(documents));

    	for ( int i = 0; i < size; i++ )
    	{
    		assertEquals(r.readDocument(null),createDocumentN(i));
    	}

    	assertTrue(r.isEOFDocument(r.readDocument(null)));

    	assertEquals(r.readDocument(null),null);
    }
    
    public void testZeroRead()
    {
    	SmallDocumentReader r = new SmallDocumentReader(new StringReader(""));
    	assertEquals(r.readDocument(null),null);
    }
    
    public void testNonsenseRead()
    {
    	SmallDocumentReader r = new SmallDocumentReader(new StringReader("asdfasdfasdfasdfasdf"));
    	assertEquals(r.readDocument(null),null);
    }
    
    private String createNDocumentsUsingWriter(int size)
    {
    	try
    	{
	    	StringWriter string_writer = new StringWriter();
	    	SmallDocumentWriter out = new SmallDocumentWriter(string_writer);
	    	
	    	for ( int i = 0; i < size; i++ )
	    	{
	    		out.writeDocument(createDocumentN(i));
	    	}
	    	
	    	out.close();
	    	
	    	return string_writer.toString();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		assert(false);
    		return null;
    	}
    }
    
    private String createDocumentN(int number)
    {
    	StringBuilder ret = new StringBuilder();
    	
    	while(ret.length() < 2048)
    	{
    		ret.append(String.format("-%d-", number));
    	}
    	
    	return ret.toString();
    }
}
