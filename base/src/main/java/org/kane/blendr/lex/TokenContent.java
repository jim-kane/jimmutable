package org.kane.blendr.lex;

import org.kane.base.serialization.Validator;

/**
 * A token that represents content (i.e. anything other than a open or close tag in blendr)
 * 
 * Final to ensure the call to complete() is safe 
 * 
 * @author jim.kane
 *
 */
final public class TokenContent extends Token
{
	private String text;
	
	public TokenContent(String text, int start_position, int length)
	{
		super(start_position,length);
		this.text = text;
		complete();
	}
	
	public void validate() 
	{
		super.validate();
		Validator.notNull(text);
	}

	public String toString() { return "CONTENT"; }
	public String getSimpleText() { return text; }
	
	
	public boolean equals(Object o) 
	{
		if ( !super.equals(o) ) return false;
		
		if ( !(o instanceof TokenContent) ) return false;
		TokenContent t = (TokenContent)o;
		
		return t.getSimpleText().equals(getSimpleText());
	}
}
