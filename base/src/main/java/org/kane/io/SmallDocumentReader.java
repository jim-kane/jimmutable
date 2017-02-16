package org.kane.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class SmallDocumentReader
{
	static public final int MAXIMUM_DELIMITER_LENGTH_IN_CHARACTERS 			= 32;
	static public final int MAXIMUM_DOCUMENT_LENGTH_IN_CHARACTERS			= 10*1024*1024; // Maximum document size, roughly 10 MB
	
	static public final String EOF_DOCUMENT = "--end-of-file--";
	static public final char[] DELIMITER_OPEN = "<?".toCharArray(); 
	static public final char[] DELIMITER_CLOSE = "?>".toCharArray();
	
	private ReadUntilReader reader;

	public SmallDocumentReader(Reader r)
	{
		reader = new ReadUntilReader(r, MAXIMUM_DELIMITER_LENGTH_IN_CHARACTERS);
	}

	public String readDocument(String default_value)
	{
		try
		{
			if ( reader.readUntil(DELIMITER_OPEN, MAXIMUM_DOCUMENT_LENGTH_IN_CHARACTERS, null) == null ) return default_value;
			
			if ( !reader.consume(DELIMITER_OPEN) ) return default_value;
			
			String delimiter_key = reader.readUntil(DELIMITER_CLOSE, MAXIMUM_DELIMITER_LENGTH_IN_CHARACTERS-4, null);
			if ( delimiter_key == null ) return default_value;
			
			if ( !reader.consume(DELIMITER_CLOSE) ) return default_value;
			
			String delimiter_str = String.format("<?%s?>", delimiter_key);
			char delimiter[] = delimiter_str.toCharArray();
			
			String document = reader.readUntil(delimiter, MAXIMUM_DOCUMENT_LENGTH_IN_CHARACTERS, null);
			if ( document == null ) return default_value;
			
			if ( !reader.consume(delimiter) ) return default_value;
			
			return document;
		}
		catch(IOException e)
		{
			return default_value;
		}
	}
	
	static public boolean isEOFDocument(String document)
	{
		if ( document == null ) return false;
		return document.equals(EOF_DOCUMENT);
	}
}
