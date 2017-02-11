package org.kane.base.blendr;

import org.kane.blendr.execute.Executor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExecuteTest extends TestCase
{
	private Executor executor = new Executor();
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ExecuteTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ExecuteTest.class );
    }
    
    public void testExecution(String code, String expected_result)
    {
    	testExecution(code,expected_result,false);
    }
    
    public void testExecution(String code, String expected_result, boolean print_diagnostic)
    {
    	try
    	{
    		String result = executor.execute(code);
    		
    		if ( print_diagnostic ) 
    			System.out.println("code: \""+code+"\", result = \""+result+"\"");
    		
    		assertEquals(result,expected_result);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		assert(false);
    	}
    }
    
    public void testBasic()
    {
    	testExecution("hello world", "hello world");
    	testExecution("{script}out.print('hello world');{/script}", "hello world");
    	testExecution("{script}out.println('hello world');{/script}", "hello world\r\n");
    	testExecution("{script}var foo = 2{/script}{!}foo{/!}", "2");
    }
    
    public void testSimpleClosure()
    {
    	StringBuilder code = new StringBuilder();
  	   
  	   code.append("{script}");
  	   
  	   code.append("var test = {html}Hello World{/html};");
  	   code.append("out.print(test);");
  	   
  	   code.append("{/script}");
  	   
  	   
  	   testExecution(code.toString(),"Hello World");
    }
    
    
    public void testStaticCodeClosure()
    {
    	StringBuilder code = new StringBuilder();
	   
	   code.append("{script}");
	   
	   code.append("var test = {!}2{/!};");
	   code.append("out.print(test);");
	   
	   code.append("{/script}");
	   
	   testExecution(code.toString(),"2");
    }
    
    public void testMultipleScripts()
    {
    	StringBuilder code = new StringBuilder();

    	code = code.append("{script}var bar = 17;{/script}");
    	code = code.append("{!}bar;{/!}");
    	
    	testExecution(code.toString(),"17");
    }
    
    public void testComplexClosure()
    {
    	StringBuilder code = new StringBuilder();

    	code.append("{script}");

    	code.append("for ( var i = 0; i < 3; i++ )");
    	code.append("{");

    	code.append("out.print({html}open {script}out.printInt(\"%,d\",i*1000);{/script} close{/html});");

    	code.append("}");

    	code.append("{/script}");

    	testExecution(code.toString(),"open 0 closeopen 1,000 closeopen 2,000 close");
    	
    }
}
