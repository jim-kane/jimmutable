package org.kane.blendr.execute;

import javax.script.ScriptException;

import org.kane.base.serialization.Validator;
import org.kane.blendr.lex.Token;
import org.kane.blendr.parse.ParseError;

/**
 * The exception classed used by executor. Includes infrastructure that
 * (hopefully) allows users to be *shown* where the error originated in their
 * code
 * 
 * 
 * @author jim.kane
 *
 */
public class ExecutionException extends RuntimeException
{
	static public int UNKNOWN_LINE_NUMBER = -1;
	static public int UNKONWN_COLUMN_NUMBER = -1;
	
	private String source_code; 
	private int line_number;
	private int column_number;
	
	/**
	 * Crate a new execution exception
	 * 
	 * @param message
	 *            The message
	 * @param source_code
	 *            The original source code.  May not be null
	 * @param line_numer
	 *            The line the error "came from", use UNKNOWN_LINE_NUMBER if you
	 *            plum don't know
	 * @param column_number
	 *            The column number where the error "came from", use
	 *            UNKONWN_COLUMN_NUMBER if you plum don't know
	 */
	public ExecutionException(String message, String source_code, int line_numer, int column_number)
	{
		super(message);
		
		this.source_code = source_code;
		this.line_number = line_numer;
		this.column_number = column_number;
		
		Validator.notNull(source_code);
	}
	
	/**
	 * Create a new exception from a ScriptException
	 * 
	 * @param script_exception The ScriptException to base this ExecutionException on.  May not be null.
	 * @param source_code The source code (may not be null)
	 */
	public ExecutionException(ScriptException script_exception, String source_code)
	{
		this( 
				script_exception.getMessage(),
				source_code,script_exception.getLineNumber(),
				script_exception.getColumnNumber()
			); 
		
		// -1 is returned by script exception for unknown line and column nubmers, so no modification is needed
	}
	
	/**
	 * Create a "chained" execution exception from any other exception
	 * 
	 * @param unknown_exception
	 *            The exception to "chain from"
	 * @param source_code
	 *            The source code that produced the exception
	 */
	public ExecutionException(Exception unknown_exception, String source_code)
	{
		super("Unkown execution exception",unknown_exception);
		
		if ( source_code == null) source_code = "Unknown source code.";
	
		this.source_code = source_code;
		
		this.line_number = UNKNOWN_LINE_NUMBER;
		this.column_number = UNKONWN_COLUMN_NUMBER;
	}
	
	/**
	 * Create a new exception from a ParseError.Exception
	 * 
	 * @param exception The ParseError.Exception to base this ExecutionException on.  May not be null.
	 * @param source_code The source code (may not be null)
	 */
	public ExecutionException(ParseError.Exception exception, String source_code)
	{
		super(exception.getMessage());
		
		this.source_code = source_code;
		
		if ( exception.hasToken() )
		{
			Token t = exception.getOptionalToken(null);
		
			line_number = column_number = 1;
			
			for ( int i = 0; i < t.getSimpleStartPosition(); i++ )
			{
				if ( i >= source_code.length() ) break; // just to be safe...
				
				char ch = source_code.charAt(i);
				if ( ch == '\n')
				{
					line_number++;
					column_number = 1;
				}
				else
				{
					column_number++;
				}
			}
		}
		
		Validator.notNull(source_code);
	}
	
	public boolean hasLineNumber() { return line_number != UNKNOWN_LINE_NUMBER; }
	public boolean hasColumnNumber() { return column_number != UNKONWN_COLUMN_NUMBER; }
	
	public int getOptionalLineNumber(int default_value) { return hasLineNumber() ? line_number : default_value; }
	public int getOptionalColumnNumber(int default_value) { return hasColumnNumber() ? column_number : default_value; }
	
	public String getSimpleSourceCode() { return source_code; }
}
