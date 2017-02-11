package org.kane.base.blendr;

import org.kane.blendr.lex.Tag;
import org.kane.blendr.parse.ParseError;
import org.kane.blendr.parse.ParseTree;
import org.kane.blendr.parse.ParseTreeContent;
import org.kane.blendr.parse.ParseTreeProgram;
import org.kane.blendr.parse.ParseTreeTag;
import org.kane.blendr.parse.Parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParseTest extends TestCase
{
	 /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParseTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ParseTest.class );
    }
    
    private ParseTree executeParser(String code, ParseError error, ParseTree default_value)
    {
    	try
    	{
    		ParseTree t = Parser.parse(code);
    		
    		assertEquals(error, null); 
    		
    		return t;
    	}
    	catch(ParseError.Exception e)
    	{
    		assertEquals(e.getSimpleError(), error);
    		return default_value;
    	}
    }

    public void testParseErrors()
    {
    	executeParser("{/html} Test", ParseError.ERROR_MIS_MATCHED_CLOSING_TAG, null);
    	executeParser("{css}{/html}", ParseError.ERROR_MIS_MATCHED_CLOSING_TAG, null);
    	
    	executeParser("{script}This is a more complicated {!}test{/css} foo bar {/script}", ParseError.ERROR_MIS_MATCHED_CLOSING_TAG, null);
    	
    	executeParser("{script}This is a more complicated {!}test", ParseError.ERROR_MISSING_CLOSING_TAG, null);
    }
    
    public void testHelloWorld()
    {
    	ParseTree t = executeParser(" Hello World ", null, null);
    	
    	assert(t instanceof ParseTreeProgram);
    	assert(t.nops() == 1);
    	
    	t = t.op(0);
    	
    	assert(t instanceof ParseTreeContent);
    	
    	assertEquals(((ParseTreeContent)t).getSimpleText(),"Hello World");
    }
    
    public void testBasicTags()
    {
    	ParseTree t = executeParser("{html} Hello World {/html}{!}9*5{/!}{css}test css{/css}{script}var = foo;{/script}", null, null);
    	
    	assert(t instanceof ParseTreeProgram);
    	assert(t.nops() == 4);
    	
    	{
    		ParseTree child = t.op(0);
    		assert(child instanceof ParseTreeTag);
    		assertEquals(((ParseTreeTag)child).getSimpleType(),Tag.OPERATOR_HTML);
    		assert(child.nops() == 1);
    		
    		child = child.op(0);

    		assert(child instanceof ParseTreeContent);
    		assertEquals(((ParseTreeContent)child).getSimpleText()," Hello World ");
    	}
    	
    	{
    		ParseTree child = t.op(1);
    		
    		assert(child instanceof ParseTreeTag);
    		assertEquals(((ParseTreeTag)child).getSimpleType(),Tag.OPERATOR_EXECUTE_SCRIPT);
    		assert(child.nops() == 1);
    		
    		child = child.op(0);

    		assert(child instanceof ParseTreeContent);
    		assertEquals(((ParseTreeContent)child).getSimpleText(),"9*5");
    	}
    	
    	{
    		ParseTree child = t.op(2);
    		
    		assert(child instanceof ParseTreeTag);
    		assertEquals(((ParseTreeTag)child).getSimpleType(),Tag.OPERATOR_CSS);
    		assert(child.nops() == 1);
    		
    		child = child.op(0);

    		assert(child instanceof ParseTreeContent);
    		assertEquals(((ParseTreeContent)child).getSimpleText(),"test css");
    	}
    	
    	{
    		ParseTree child = t.op(3);
    		
    		assert(child instanceof ParseTreeTag);
    		assertEquals(((ParseTreeTag)child).getSimpleType(),Tag.OPERATOR_SCRIPT);
    		assert(child.nops() == 1);
    		
    		child = child.op(0);

    		assert(child instanceof ParseTreeContent);
    		assertEquals(((ParseTreeContent)child).getSimpleText(),"var = foo;");
    	}
    }
    
    
    
   /* public void testBasicParse()
    {
    	try
    	{
    		ParseTree t = Parser.parse("{html}Hello World {script}var foo = {html}some html content{/html}{/script} some more html content{/html}");
    		
    		System.out.println(t);
    	}
    	catch(ParseError.Exception e)
    	{
    		e.printStackTrace();
    	}
    }*/
}
