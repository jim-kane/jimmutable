package org.kane.blendr.lex;

/**
 * An enum of all of the valid tags in the blendr language
 * 
 * @author jim.kane
 *
 */
public enum Tag 
{
	OPERATOR_SCRIPT("script"),
	OPERATOR_EXECUTE_SCRIPT("!"),
	OPERATOR_HTML("html"),
	OPERATOR_CSS("css");
	
	
	private char[] open_characters;
	private char[] close_characters;
	
	private String open_string;
	private String close_string;
	
	private Tag(String str)
	{
		open_string = String.format("{%s}", str);
		close_string = String.format("{/%s}", str);
		
		this.open_characters = open_string.toCharArray();
		this.close_characters = close_string.toCharArray();
	}
	
	public String toString() { return open_string; }
	
	/**
	 * Is input at the open version of this tag?
	 * 
	 * @param input The input stream to test
	 * @return True if input is at the open tag, false otherwise
	 */
	public boolean atOpen(LexStream input)
	{
		if ( input == null ) return false;
		
		return input.at(open_characters);
	}
	
	/**
	 * Is input at the close version of this tag?
	 * 
	 * @param input The input stream to test
	 * @return True if input is at the close tag, false otherwise
	 */
	public boolean atClose(LexStream input)
	{
		if ( input == null ) return false;
		
		return input.at(close_characters);
	}
	
	/**
	 * Eat the open tag
	 * @param input The input stream
	 */
	public void eatOpen(LexStream input)
	{
		if ( input == null ) return;
		input.eat(open_characters.length);
	}
	
	/**
	 * Eat the close tag
	 * @param input The input stream
	 */
	public void eatClose(LexStream input)
	{
		if ( input == null ) return;
		input.eat(close_characters.length);
	}
	
	public String getSimpleOpenString() { return open_string; }
	public String getSimpleCloseString() { return close_string; }
	
	/**
	 * Create an open tag token
	 * @param position The position (in the source code) of the start of the token
	 * @return A open tag token
	 */
	public TokenOpenTag createOpenToken(int position)
	{
		return new TokenOpenTag(this,position);
	}
	
	/**
	 * Create an close tag token
	 * @param position The position (in the source code) of the start of the token
	 * @return A close tag token
	 */
	public TokenCloseTag createCloseToken(int position)
	{
		return new TokenCloseTag(this,position);
	}
}
