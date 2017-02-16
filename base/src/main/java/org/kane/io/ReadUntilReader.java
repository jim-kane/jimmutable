package org.kane.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class ReadUntilReader extends PushbackReader
{
	private int maximum_marker_size;
	
	/**
	 * Create a ReadUntilReader
	 * 
	 * @param r The reader to use as the source of data
	 * @param maximum_marker_size The maximum size of marker 
	 */
	public ReadUntilReader(Reader r, int maximum_marker_size)
	{
		super(r,maximum_marker_size);
		this.maximum_marker_size = maximum_marker_size;
	}
	
	/**
	 * Test a StringBuilder to see if it ends with a certain string
	 * 
	 * @param builder A StringBuilder object
	 * @param marker The String (as a character array) to test against
	 * @return True if builder ends with marker, false otherwise
	 */
	static public boolean endsWith(StringBuilder builder, char marker[])
	{
		if ( builder == null || marker == null ) return false;
		if ( builder.length() < marker.length ) return false;


		int start_pos = builder.length()-marker.length;

		for ( int i = 0; i < marker.length; i++ )
		{
			if ( builder.charAt(start_pos+i) != marker[i] ) return false;
		}

		return true;
	}
	
	/**
	 * Advance the reader until it is positioned at the start of a given string.
	 * In the event that the read_until fails, the reader is advanced an unknown
	 * number of bytes in the unput...
	 * 
	 * @param marker
	 *            The string to advance the reader until
	 * @param read_limit
	 *            The maximum number of characters to consume in search of
	 *            marker. If marker is not found after reading this many
	 *            characters, default_value is returned
	 * @param default_value
	 *            The value to return if marker can not be found before
	 *            read_limit or eof
	 * @return A string containing all characters read between the current
	 *         position and marker, or default_value if marker could not be
	 *         found within the read_limit
	 * @throws IOException
	 */
	public String readUntil(char marker[], int read_limit, String default_value) throws IOException
	{
		StringBuilder ret = new StringBuilder();

		while(true)
		{
			if ( ret.length() > read_limit ) return default_value;

			if ( endsWith(ret,marker) )
			{
				unread(marker);

				return ret.substring(0, ret.length()-marker.length);
			}

			int char_code = read();

			if ( char_code == -1 ) // eof
				return default_value;

			ret.append((char)char_code);
		}
	}

	/**
	 * Returns true if the reader is at the specified characters, false otherwise.
	 * 
	 * @param marker
	 *            The string (in character array form) test against
	 *            
	 * @return true if the reader is at the specified characters, false otherwise
	 *            
	 * @throws IOException
	 */
	public boolean at(char marker[]) throws IOException
	{
		if ( marker == null ) return false;
		if ( marker.length > maximum_marker_size ) return false;
		if ( marker.length == 0 ) return true;
		
		char tmp[] = new char[marker.length];
		
		int amount_read = read(tmp);
		
		if ( amount_read == -1 ) 
			return false;
		
		unread(tmp); // put it back, no matter what
		
		if ( amount_read != marker.length )
		{
			return false;
		}
		
		for ( int i = 0; i < marker.length; i++ )
		{
			if ( marker[i] != tmp[i] ) 
				return false;
		}
		
		return true;
	}
	
	/**
	 * If the reader is positioned at marker, consume that many bytes, return
	 * true. Otherwise return false
	 * 
	 * @param marker
	 *            The marker to test for
	 * @return True if at marker, false otherwise
	 */
	public boolean consume(char marker[]) throws IOException
	{
		if (!at(marker) ) return false;
		
		skip(marker.length);
		return true;
	}
}
