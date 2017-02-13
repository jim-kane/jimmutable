package org.kane.blendr.lex;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.*;

/**
 * The abstract base class representing a Token
 * 
 * @author jim.kane
 *
 */
abstract public class Token extends StandardImmutableObject
{
	private int start_position;
	private int length;
	
	/**
	 * Create a new token
	 * 
	 * @param start_position The start of the token in the source code
	 * @param length The length of the token in the source code
	 */
	public Token(int start_position, int length)
	{
		this.start_position = start_position;
		this.length = length;
	}
	
	public int getSimpleStartPosition() { return start_position; }
	public int getSimpleLength() { return length; }
	
	public void normalize() 
	{	
	}

	public void validate() 
	{
		Validator.min(start_position, 0);
		Validator.min(length, 0);
	}

	public int compareTo(Object o) 
	{
		if ( !(o instanceof Token) ) return 0;
		Token t = (Token)o;
		
		int compare;
		
		compare = Integer.compare(getSimpleStartPosition(), t.getSimpleStartPosition());
		if ( compare != 0 ) return compare;
		
		return Integer.compare(getSimpleLength(), t.getSimpleLength());
	}
	
	public int hashCode()
	{
		return toString().hashCode();
	}

	public boolean equals(Object o) 
	{
		if ( !(o instanceof Token) ) return false;
		Token t = (Token)o;
		
		if ( getSimpleStartPosition() != t.getSimpleStartPosition() ) return false;
		if ( getSimpleLength() != t.getSimpleLength() ) return false;
		
		return true;
	}
}
