package org.kane.blendr.lex;

import org.kane.base.serialization.Validator;

/**
 * A token that represents a close tag (e.g. {/script} or {/html}
 * 
 * @author jim.kane
 *
 */
final public class TokenOpenTag extends Token
{
	private Tag operator;
	
	public TokenOpenTag(Tag operator, int start_position)
	{
		super(start_position,operator.getSimpleOpenString().length());
		this.operator = operator;
		complete();
	}
	
	public void validate() 
	{
		super.validate();
		Validator.notNull(operator);
	}
	
	public void freeze()
	{
		
	}

	public String toString() { return operator.getSimpleOpenString(); }
	public Tag getSimpleOperator() { return operator; }
	
	
	public boolean equals(Object o) 
	{
		if ( !super.equals(o) ) return false;
		
		if ( !(o instanceof TokenOpenTag) ) return false;
		
		TokenOpenTag t = (TokenOpenTag)o;
		
		return getSimpleOperator().equals(t.getSimpleOperator());
	}
}
