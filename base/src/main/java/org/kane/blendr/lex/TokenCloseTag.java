package org.kane.blendr.lex;

import org.kane.base.serialization.Validator;

/**
 * A token that represents a open tag (e.g. {script} or {html}
 * 
 * Final to ensure the call to complete() is safe 
 * 
 * @author jim.kane
 *
 */
final public class TokenCloseTag extends Token
{
	private Tag operator;
	
	public TokenCloseTag(Tag operator, int start_position)
	{
		super(start_position,operator.getSimpleCloseString().length());
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

	public String toString() { return operator.getSimpleCloseString(); }
	public Tag getSimpleOperator() { return operator; }
	
	public boolean equals(Object o) 
	{
		if ( !super.equals(o) ) return false;
		
		if ( !(o instanceof TokenCloseTag) ) return false;
		
		TokenCloseTag t = (TokenCloseTag)o;
		
		return getSimpleOperator().equals(t.getSimpleOperator());
	}
}
