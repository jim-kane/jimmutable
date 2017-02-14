package org.kane.blendr.parse;

import java.util.Collections;

import org.kane.base.serialization.Validator;
import org.kane.blendr.execute.Executor;
import org.kane.blendr.lex.TokenContent;

/**
 * A parse tree node used to represent content (e.g. something other than a tag)
 * in a blendr program
 * 
 * @author jim.kane
 *
 */
final public class ParseTreeContent extends ParseTree
{
	private String text;
	
	protected ParseTreeContent(ParseTree parent, String text)
	{
		super(parent, Collections.EMPTY_LIST);
		this.text = text;
		
		complete();
	}
	
	public void normalize()
	{
		
	}
	
	public void validate()
	{
		super.validate();
		Validator.notNull(text);
	}
	
	public void freeze()
	{
		super.freeze();
	}
	
	public String getSimpleText() { return text; }
	
	public int hashCode() 
	{
		return text.hashCode();
	}

	
	public boolean nodeEquals(ParseTree node) 
	{
		if ( !(node instanceof ParseTreeContent) ) return false;
		
		ParseTreeContent other = (ParseTreeContent)node;
		return text.equals(other.text);
	}
	
	public String getToStringLabel()
	{
		return "CONTENT";
	}
}
