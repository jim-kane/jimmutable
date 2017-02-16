package org.kane.base.io;

import java.io.IOException;
import java.io.StringReader;

import org.kane.io.ReadUntilReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ReadUntilReaderTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ReadUntilReaderTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ReadUntilReaderTest.class );
    }

    
    public void testEndsWith()
    {
    	assertFalse(ReadUntilReader.endsWith(null, null));
    	assertFalse(ReadUntilReader.endsWith(new StringBuilder(), null));
    	assertFalse(ReadUntilReader.endsWith(null, "foo".toCharArray()));
    	
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("foo"), "foo".toCharArray()));
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("?>"), "?>".toCharArray()));
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("<?hello?>"), "?>".toCharArray()));
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("<?hello?>"), ">".toCharArray()));
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("<?hello?>"), "".toCharArray()));
        assertTrue(ReadUntilReader.endsWith(new StringBuilder("a"), "a".toCharArray()));
        
        assertFalse(ReadUntilReader.endsWith(new StringBuilder("foo"), "foobar".toCharArray()));
        assertFalse(ReadUntilReader.endsWith(new StringBuilder("aaa"), "bbb".toCharArray()));
        assertFalse(ReadUntilReader.endsWith(new StringBuilder("a"), "b".toCharArray()));
        assertFalse(ReadUntilReader.endsWith(new StringBuilder(""), "b".toCharArray()));
    }
    
    public void testAt()
    {
    	String test = "ABCDEFG";
    	ReadUntilReader reader = new ReadUntilReader(new StringReader(test),25);
    	
    	try
    	{
    		assertFalse(reader.at(null));
    		
    		assertTrue(reader.at("".toCharArray()));
    		assertTrue(reader.at("A".toCharArray()));
    		assertTrue(reader.at("AB".toCharArray()));
    		assertTrue(reader.at("ABC".toCharArray()));
    		assertTrue(reader.at("ABCD".toCharArray()));
    		assertTrue(reader.at("ABCDE".toCharArray()));
    		assertTrue(reader.at("ABCDEF".toCharArray()));
    		assertTrue(reader.at("ABCDEFG".toCharArray()));
    		
    		assertFalse(reader.at("ABCDEFGH".toCharArray()));
    		assertFalse(reader.at("B".toCharArray()));
    		assertFalse(reader.at("THIS STRING IS TOO LONG AS IT IS LONGER THAN THE VALUE SPECIFIED AS THE MAXIMUM MARKER LENGTH".toCharArray()));
    		
    		reader.skip(1);
    		
    		assertTrue(reader.at("".toCharArray()));
    		assertTrue(reader.at("B".toCharArray()));
    		assertTrue(reader.at("BC".toCharArray()));
    		assertTrue(reader.at("BCD".toCharArray()));
    		assertTrue(reader.at("BCDE".toCharArray()));
    		assertTrue(reader.at("BCDEF".toCharArray()));
    		assertTrue(reader.at("BCDEFG".toCharArray()));
    		
    		assertFalse(reader.at("BCDEFGH".toCharArray()));
    		assertFalse(reader.at("C".toCharArray()));
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		assert(false);
    	}
    }
    
    public void testReadUntil()
    {
    	String test = " <?hi?>This is a test<?hi?>";
    	ReadUntilReader reader = new ReadUntilReader(new StringReader(test),25);
    	
    	try
    	{
    		assertEquals(reader.readUntil("<?hi?>".toCharArray(), 25, null), " ");
    		assertTrue(reader.consume("<?hi?>".toCharArray()));
    		assertEquals(reader.readUntil("<?hi?>".toCharArray(), 25, null), "This is a test");
    		assertTrue(reader.consume("<?hi?>".toCharArray()));
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		assert(false);
    	}
    	
    	
    	test = " <?hi?>This is a test<?hi?>";
    	reader = new ReadUntilReader(new StringReader(test),25);
    	
    	try
    	{
    		assertEquals(reader.readUntil("<?hi?>".toCharArray(), 25, null), " ");
    		assertTrue(reader.consume("<?hi?>".toCharArray()));
    		assertEquals(reader.readUntil("<?hi?>".toCharArray(), 4, null), null); // Could not find <?hi?> within the alloted read ahead distance...
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		assert(false);
    	}
    }
}
