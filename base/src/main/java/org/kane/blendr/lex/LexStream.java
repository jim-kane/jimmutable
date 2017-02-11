package org.kane.blendr.lex;

/**
 * A class used by Lexer to create a psuedo stream for lexographical analysis
 * 
 * 
 * @author jim.kane
 *
 */
public class LexStream 
{
	private char data[];
	private int position;
	
	/**
	 * Create a new LexStream from a string
	 * 
	 * @param str The original source code to lex
	 */
	public LexStream(String str)
	{
		if ( str == null ) str = "";
		data = str.toCharArray();
		position = 0;
	} 
	
	/**
	 * Am I at a given string?
	 * 
	 * @param characters
	 *            The characters of the string to test for
	 * @return true if the LexStream is at the string (specified by characters),
	 *         false otherwise. False if characters is null.
	 */
	public boolean at(char []characters)
	{
		if ( characters == null ) return false;
		
		if ( length() < characters.length ) return false;
		
		for ( int i = 0; i < characters.length; i++ )
		{
			if ( characters[i] != data[position+i] ) return false;
		}
		
		return true;
	}
	
	/**
	 * Get the character at the current position + idx
	 * 
	 * @param idx
	 *            The offset from the current position to "read"
	 * 
	 * @return The character at the specified offset. Throws array out of bounds
	 *         exceptions if you are not careful.
	 */
	public char charAt(int idx)
	{
		return data[position+idx];
	}
	
	/**
	 * Eat the current character (advance the position to the next character)
	 */
	public void eat()
	{
		position++;
		if ( position > data.length ) position = data.length;
	}
	
	/**
	 * Eat the next n characters. Equivilent to calling eat() n times...
	 * 
	 * @param n
	 *            The number of characters to eat
	 */
	public void eat(int n)
	{
		position += n;
		if ( position > data.length ) position = data.length;
	} 

	/**
	 * Get the length of the *remaining* input
	 * 
	 * @return The length of the remaingin (not eaten) input.
	 */
	private int length()
	{
		return data.length-position;
	}
	
	/**
	 * Has everything been eaten?
	 * 
	 * @return True if there are still characters to "eat", false otherwise
	 */
	public boolean isEmpty() { return position >= data.length; }
	
	/**
	 * Get the current "position" in input
	 * 
	 * @return The current position in input
	 */
	public int getPosition() { return position; }
}
