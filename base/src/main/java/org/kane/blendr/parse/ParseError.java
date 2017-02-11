package org.kane.blendr.parse;

import org.kane.base.serialization.Validator;
import org.kane.blendr.lex.Token;

/**
 * Class used to enumerate the parse errors that are possible when parsing a
 * blendr program
 * 
 * @author jim.kane
 *
 */
public enum ParseError 
{
	ERROR_MIS_MATCHED_CLOSING_TAG("Close tag does not have a matching open tag"),
	ERROR_UNKNOWN_TOKEN_TYPE("Unknown token type"),
	ERROR_MISSING_CLOSING_TAG("One or more tags that were opened in this document remain unclosed at the end");
	
	private String message;
	
	private ParseError(String message)
	{
		Validator.notNull(message);
		this.message = message;
	}
	
	/**
	 * Get a human readable description of the error
	 * @return Returns an human readable description of the error
	 */
	public String getSimpleMessage() { return message; }
	
	/**
	 * Throw an exception (based on the error)
	 * 
	 * @param token The token that produced the errror
	 * @throws Exception 
	 */
	public void throwException(Token token) throws Exception
	{
		throw new Exception(this, token);
	}
	
	
	static public class Exception extends java.lang.Exception
	{
		private ParseError error;
		private Token token;
		
		private Exception(ParseError error, Token t)
		{
			super(error.getSimpleMessage());
			
			Validator.notNull(error);
			
			this.error = error;
			this.token = t;
		}
		
		public ParseError getSimpleError()
		{
			return error;
		}
		
		public Token getOptionalToken(Token default_value)
		{
			if ( token == null ) return default_value;
			return token;
		}
		
		public boolean hasToken() { return token != null; }
		
	}
}
