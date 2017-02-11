package org.kane.blendr.parse;

import java.util.List;

import org.kane.base.serialization.Validator;
import org.kane.blendr.execute.Executor;
import org.kane.blendr.lex.Tag;

/**
 * A parse tree node used to represent a tag (e.g. {script} or {html}) in a blendr program
 * @author jim.kane
 *
 */
public class ParseTreeTag extends ParseTree
{
	private Tag type;
	
	protected ParseTreeTag(ParseTree.Builder builder, ParseTree parent, Tag type)
	{
		super(builder, parent);
		this.type = type;
		
	}
	
	public ParseTreeTag(ParseTree parent, Tag type, List<ParseTree> children)
	{
		super(parent, children);
		this.type = type;
		
		complete();
	}
	
	public void validate()
	{
		super.validate();
		Validator.notNull(type);
	}
	
	public void normalize()
	{
		
	}
	
	public Tag getSimpleType() { return type; }

	
	public boolean nodeEquals(ParseTree node) 
	{
		if ( !(node instanceof ParseTreeTag) ) return false;
		
		ParseTreeTag other = (ParseTreeTag)node;
		return getSimpleType().equals(other.getSimpleType());
	}
	
	public String getToStringLabel()
	{
		return type.getSimpleOpenString();
	}
}
