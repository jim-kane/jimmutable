package org.kane.base.blendr;

import org.kane.base.serialization.JavaCodeUtils;
import org.kane.base.serialization.StandardObject;
import org.kane.blendr.lex.LexOutput;
import org.kane.blendr.lex.Lexer;
import org.kane.blendr.lex.Tag;
import org.kane.blendr.lex.Token;
import org.kane.blendr.lex.TokenCloseTag;
import org.kane.blendr.lex.TokenOpenTag;
import org.kane.blendr.lex.TokenContent;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LexTest extends TestCase
{
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LexTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LexTest.class );
    }

    public void testTextOnlyLex()
    {
    	String original_source = "Hello World";
    	
    	LexOutput output = Lexer.lex(original_source);
    	
    	// Construct lex by hand
    	LexOutput.Builder builder = new LexOutput.Builder(original_source);
    	builder.addToken(new TokenContent(original_source,0,original_source.length()));
    	
    	// Assert that the lexer and the by hand lex are equal
    	assertEquals(output,builder.create());
    }
    
    public void testTokenObjects()
    {
    	Token one;
    	Token two;
    	
    	
    	{
    		one = new TokenContent("Hello World", 0, "Hello World".length());
    		two = new TokenContent("Hello World", 0, "Hello World".length());
    		
    		assertEquals(one,two);
    	}
    	
    	{
    		one = new TokenContent("Hello World", 0, "Hello World".length());
    		two = new TokenContent("Hello World!", 0, "Hello World!".length());
    		
    		assert(!one.equals(two));
    	}
    	
    	{
    		one = new TokenContent("Hello World", 0, "Hello World".length());
    		two = new TokenContent("Hello World", 10, "Hello World".length());
    		
    		assert(!one.equals(two));
    	}
    	
    	{
    		one = new TokenOpenTag(Tag.OPERATOR_HTML,0);
    		two = new TokenOpenTag(Tag.OPERATOR_HTML,0);
    		
    		assertEquals(one,two);
    		
    		assertEquals(((TokenOpenTag)one).getSimpleOperator(),Tag.OPERATOR_HTML);
    	}
    	
    	{
    		one = new TokenCloseTag(Tag.OPERATOR_HTML,0);
    		two = new TokenCloseTag(Tag.OPERATOR_HTML,0);
    		
    		assertEquals(one,two);
    		
    		assertEquals(((TokenCloseTag)one).getSimpleOperator(),Tag.OPERATOR_HTML);
    	}
    	
    	{
    		one = new TokenCloseTag(Tag.OPERATOR_HTML,0);
    		two = new TokenOpenTag(Tag.OPERATOR_HTML,0);
    		
    		assert(!one.equals(two));
    	}
    }
    
    
    public void testCompleteLex()
    {
    	String original_source = " {html}This is a test {script} var foo = {html}bar{/html} {/script}.<br/>Test execution: {!}foo{/!}.{/html} ";
    	
    	LexOutput from_lex = Lexer.lex(original_source);
    	
    	// Construct lex by hand
    	LexOutput.Builder builder = new LexOutput.Builder(original_source);
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenOpenTag\":{\"start_position\":1,\"length\":6,\"operator\":\"OPERATOR_HTML\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":7,\"length\":15,\"text\":\"This is a test \"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenOpenTag\":{\"start_position\":22,\"length\":8,\"operator\":\"OPERATOR_SCRIPT\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":30,\"length\":11,\"text\":\" var foo = \"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenOpenTag\":{\"start_position\":41,\"length\":6,\"operator\":\"OPERATOR_HTML\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":47,\"length\":3,\"text\":\"bar\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenCloseTag\":{\"start_position\":50,\"length\":7,\"operator\":\"OPERATOR_HTML\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":57,\"length\":1,\"text\":\" \"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenCloseTag\":{\"start_position\":58,\"length\":9,\"operator\":\"OPERATOR_SCRIPT\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":67,\"length\":22,\"text\":\".<br\\/>Test execution: \"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenOpenTag\":{\"start_position\":89,\"length\":3,\"operator\":\"OPERATOR_EXECUTE_SCRIPT\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":92,\"length\":3,\"text\":\"foo\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenCloseTag\":{\"start_position\":95,\"length\":4,\"operator\":\"OPERATOR_EXECUTE_SCRIPT\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenContent\":{\"start_position\":99,\"length\":1,\"text\":\".\"}}", null));
    	builder.addToken((Token)StandardObject.quietFromJSON("{\"org.kane.blendr.lex.TokenCloseTag\":{\"start_position\":100,\"length\":7,\"operator\":\"OPERATOR_HTML\"}}", null));
    		
    	//System.out.println(lexOutputToStatements(from_lex));
    	
    	LexOutput from_builder = builder.create();
    	
    	// Assert that the lexer and the by hand lex are equals
    	assertEquals(from_lex,from_builder);
    }
    
    
    static private String lexOutputToStatements(LexOutput output)
    {
    	StringBuilder ret = new StringBuilder();
    	
    	
    	for ( Token t : output.getSimpleTokens() )
    	{
    		ret.append(String.format("builder.addToken((Token)StandardObject.quietFromJSON(%s, null));\n", JavaCodeUtils.toJavaStringLiteral(t.toJSON())));
    	}
    	
    	return ret.toString();
    }
    
    
 
}
