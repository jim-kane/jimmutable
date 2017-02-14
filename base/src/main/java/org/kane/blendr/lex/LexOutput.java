package org.kane.blendr.lex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.serialization.Validator;

/**
 * An encapsulation of the output of the blendr lexer (lexographical analyzer)
 * 
 * @author jim.kane
 *
 */
public class LexOutput extends StandardImmutableObject
{
	private String original_source_code;
	private FieldList<Token> tokens;
	
	/**
	 * Constructor used by the builder
	 * 
	 * @param builder
	 *            The builder that is creating this LexOutput object
	 */
	private LexOutput(Builder builder)
	{
		// constructor used by builder...
		this.original_source_code = "";
		this.tokens = new FieldArrayList();
		
		// No call to complete, as this constructor is used by the builder...
	}
	
	/**
	 * Create a LexOutput object by supplying both the original source code and
	 * the list of tokens
	 * 
	 * @param original_code
	 *            The orignial source code
	 * @param tokens
	 *            The list of tokens "lexed" from the source code
	 */
	public LexOutput(String original_code, List<Token> tokens)
	{
		this.original_source_code = original_code;
		this.tokens = new FieldArrayList(tokens);
		
		complete();
	}
	
	public void normalize() 
	{
		
	}

	public void validate() 
	{
		Validator.notNull(original_source_code);
		Validator.notNull(tokens);
	}
	
	public void freeze()
	{
		tokens.freeze();
	}

	public String getSimpleOriginalSourceCode() { return original_source_code; }
	public List<Token> getSimpleTokens() { return tokens; }

	public int compareTo(Object o) 
	{
		if ( !(o instanceof LexOutput) ) return 0;
		LexOutput other = (LexOutput)o;
		
		return getSimpleOriginalSourceCode().compareTo(other.getSimpleOriginalSourceCode());
	}

	
	public int hashCode() 
	{
		return getSimpleOriginalSourceCode().hashCode();
	}

	
	public boolean equals(Object o) 
	{
		if ( !(o instanceof LexOutput) ) return false;
		LexOutput other = (LexOutput)o;
		
		if ( !getSimpleOriginalSourceCode().equals(other.getSimpleOriginalSourceCode()) ) return false;
		
		return tokens.equals(other.getSimpleTokens());
	}
	
	public String toString() 
	{
		return tokens.toString();
	}
	
	/**
	 * Builder class used to create LexOutput objects
	 * 
	 * @author jim.kane
	 *
	 */
	static public class Builder
	{
		private LexOutput under_construction;
		
		public Builder(String original_source_code) 
		{
			under_construction = new LexOutput(this);
			setOriginalSourceCode(original_source_code);
		}
		
		public void addToken(Token t)
		{
			under_construction.assertNotComplete();
			if ( t == null ) return;
			
			under_construction.tokens.add(t);
		}
		
		public void setOriginalSourceCode(String original_source_code)
		{
			under_construction.assertNotComplete();
			under_construction.original_source_code = original_source_code;
		}
		
		public LexOutput create() 
		{ 
			under_construction.complete();
			
			return under_construction; // safe to do, it is now complete...
		}
	}
}
