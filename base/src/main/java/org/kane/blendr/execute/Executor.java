package org.kane.blendr.execute;

import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Supplier;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.kane.base.serialization.collections.LRUCache;
import org.kane.blendr.lex.Tag;
import org.kane.blendr.parse.ParseError;
import org.kane.blendr.parse.ParseTree;
import org.kane.blendr.parse.ParseTreeContent;
import org.kane.blendr.parse.ParseTreeProgram;
import org.kane.blendr.parse.ParseTreeTag;
import org.kane.blendr.parse.Parser;

import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

// TODO: Contain the CPU that a script can use (https://github.com/javadelight/delight-nashorn-sandbox)
// TODO: Contain the memory that a script can use

/**
 * An executor capable of quickly parsing and executing bendr scripts.
 * 
 * Executors *are not thread safe*. Do not use in multiple threads
 * simultaneously.
 * 
 * Executors are *expensive* to make. Cache and reuse them. In most
 * applications, creating one executor per thread in a thread pool (store in a
 * ThreadLocal) is the way to go.
 * 
 * @author jim.kane
 *
 */
public class Executor 
{
	static private LRUCache<String,ParseTree> cache_blendr_parse = new LRUCache(10_000);
	static private LRUCache<String,CompiledScript> cache_script = new LRUCache(10_000);
	
	private ScriptEngine engine;
	private Bindings clean_engine_scope_bindings, clean_global_scope_bindings;
	
	private long next_closure_id = 1;
	
	private String overall_result;
	
	/**
	 * Create a new executor. Creating executors is *expensive* -- save them an
	 * reuse them. Do not use them across threads
	 */
	public Executor()
	{
		// Setup the script environment
		NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
		engine = factory.getScriptEngine(new ExecutionEnvironmentClassFilter());

		// create the clean bindings
		clean_engine_scope_bindings = engine.createBindings();
		clean_engine_scope_bindings.put("out", new OutputFunctions(this));
		
		clean_global_scope_bindings = engine.createBindings();
		
		resetScriptEngine();
	}

	/**
	 * Blocks access to all java classes/methods
	 * 
	 * @author jim.kane
	 *
	 */
	static private class ExecutionEnvironmentClassFilter implements ClassFilter 
	{
		public boolean exposeToScripts(String s) 
		{
			return false;
		}
	}
	
	/**
	 * Reset the script engine (to its base state). Called before each document
	 * is executed...
	 */
	private void resetScriptEngine()
	{
		engine.setBindings(clean_engine_scope_bindings, ScriptContext.ENGINE_SCOPE);
		engine.setBindings(clean_global_scope_bindings, ScriptContext.GLOBAL_SCOPE);
	}
	
	/**
	 * Parse and execute some blendr source code
	 * 
	 * @param source_code
	 *            The source code to run (may not be null)
	 * @return The result of the executed source code
	 * 
	 * @throws ExecutionException
	 *             The only exception that can be emitted is an Execution
	 *             exception. All other exceptions are swallowed and chained
	 *             into an execution exception
	 */
	public String execute(String source_code) throws ExecutionException
	{
		try
		{
			if ( source_code == null ) 
				throw new ExecutionException("Attempt to execute null source code", "source_code was null!", ExecutionException.UNKNOWN_LINE_NUMBER, ExecutionException.UNKONWN_COLUMN_NUMBER);
			
			ParseTree t = cache_blendr_parse.get(source_code,null);
			
			if ( t == null ) 
			{
				try
				{
					t = Parser.parse(source_code);
					cache_blendr_parse.put(source_code, t);
				}
				catch(ParseError.Exception e)
				{
					throw new ExecutionException(e,source_code);
				}
			}
			
			return execute(t, source_code);
		}
		catch(ExecutionException execution_exception)
		{
			throw execution_exception;
		}
		catch(Exception unknown_exception)
		{
			throw new ExecutionException(unknown_exception, source_code);
		}
	}
	
	/**
	 * Execute an previously parsed document.
	 * 
	 * @param t The parse tree to execute
	 * @param source_code the source_code t was compiled from.  Null is allowed
	 * 
	 * @return The result of the executed script
	 * @throws ExecutionException
	 *             The only exception that can be emitted is an Execution
	 *             exception. All other exceptions are swallowed and chained
	 *             into an execution exception
	 */
	public String execute(ParseTree t, String source_code) throws ExecutionException
	{
		try
		{
			StringWriter output = new StringWriter();
			executeIter(t, output);
			try { output.close(); } catch(Exception e) {}
			
			return output.toString();
		}
		catch(ExecutionException execution_exception)
		{
			throw execution_exception;
		}
		catch(Exception unknown_exception)
		{
			throw new ExecutionException(unknown_exception, source_code);
		}
	}
	
	/**
	 * Do the heavy lifting of executing a parse tree
	 * 
	 * @param t The parse tree to execute
	 * @param output The output writer that will receive all output
	 * @throws ExecutionException
	 */
	private void executeIter(ParseTree t, StringWriter output) throws ExecutionException
	{
		if ( t instanceof ParseTreeContent )
		{
			ParseTreeContent content = (ParseTreeContent)t;
			
			output.write(content.getSimpleText());
			return;
		}
		
		if ( t instanceof ParseTreeProgram )
		{
			for ( ParseTree child : t.getSimpleChildren() )
			{
				executeIter(child, output);
			}
			
			return;
		}
		
		if ( t instanceof ParseTreeTag )
		{
			ParseTreeTag tag = (ParseTreeTag)t;
			
			if ( tag.getSimpleType().equals(Tag.OPERATOR_SCRIPT) || tag.getSimpleType().equals(Tag.OPERATOR_EXECUTE_SCRIPT) )
			{
				StringBuilder script = new StringBuilder();
				
				for ( ParseTree child : t.getSimpleChildren() )
				{
					if ( child instanceof ParseTreeContent) 
					{
						ParseTreeContent content = (ParseTreeContent)child;
						script.append(content.getSimpleText());
					}
					else
					{
						String closure_id = "blendr_closure"+createUniqueClosureID();


						engine.put(closure_id, new ExecuteLater(child));
						
						script.append(closure_id);
						script.append("()");
					}
				}
				
				
				 ScriptContext context = engine.getContext();
		    	 context.setWriter(output);
		    	 
		    	 
		    	 try 
		    	 { 
		    		 //System.out.println("exec: "+script);
		    		 String script_str = script.toString();
		    		
		    		 CompiledScript compiled_script = cache_script.get(script_str,null);
		    		 
		    		 if ( compiled_script == null )
		    		 {
		    			 compiled_script = ((Compilable)engine).compile(script_str);
		    			 cache_script.put(script_str, compiled_script);
		    		 }
		    		 
		    		 Object result = compiled_script.eval();
		    		 
		    		 //System.out.println("result: "+result);
		    		 
		    		 if ( tag.getSimpleType().equals(Tag.OPERATOR_EXECUTE_SCRIPT) && result != null )
		    		 {
						output.append(result.toString());
		    		 }
		    	 } 
		    	 catch(ScriptException e) 
		    	 { 
		    		throw new ExecutionException(e,script.toString());
		    	 }
		    	 
		    	 return;
			}
			else
			{
				for ( ParseTree child : t.getSimpleChildren() )
				{
					executeIter(child, output);
				}
				
				return;
			}
		}
		
		// This error should never, strictly speaking, happen... only if a new parse tree type is made and not added to the executor...
		throw new ExecutionException("Unown parse tree type: "+t.getClass(), "unkonwn source code", ExecutionException.UNKNOWN_LINE_NUMBER, ExecutionException.UNKONWN_COLUMN_NUMBER);
	}
	
	/**
	 * This class is used to form a "closure". When you use a server-side tag
	 * inside of a script, blendr forms a closure for you (executing the code
	 * later).  For example, consider the following code:
	 * 
	 * {script}
	 * for ( var i = 0; i < 10; i++ )
	 * {
	 * 		out.println({html}<b>{!}i{/!}</b>{/html});
	 * }
	 * {/script}
	 * 
	 * This code is rewritten as:
	 * 
	 * for ( var i = 0; i < 10; i++ )
	 * {
	 * 		out.println(blendr_closure1());
	 * }
	 * 
	 * before execution. The  blendr_closure1 function is created by the system and will, on command, execute {html}<b>{!}i{/!}</b>{/html}.  Neat, hunh?
	 * 
	 * Wll, the execute later class is how this all works under the hood...
	 * 
	 * @author jim.kane
	 *
	 */
	private class ExecuteLater implements Supplier<String> 
	{
		private ParseTree contents;


		public ExecuteLater(ParseTree contents)
		{
			this.contents = contents;
		}

		public String get()
		{
			StringWriter output = new StringWriter();

			Writer old_out = engine.getContext().getWriter();
			
			executeIter(contents,output);
			engine.getContext().setWriter(old_out);

			try { output.close(); } catch(Exception e) {}

			String ret = output.toString();
			return ret;
		}
	}

	/**
	 * Create a unique closure ID
	 * 
	 * @return A unique closure ID
	 */
	private long createUniqueClosureID() { return Math.abs(next_closure_id++); }

	protected ScriptEngine getSimpleScriptEngine()
	{
		return engine;
	}
}
