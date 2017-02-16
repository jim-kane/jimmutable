package org.kane.io;

import java.io.IOException;
import java.io.Writer;

import org.kane.db_experiments.RandomData;

public class SmallDocumentWriter
{
	
	private Writer out;
	private RandomData random_data = new RandomData();
	
	public SmallDocumentWriter(Writer out)
	{
		this.out = out;
	}
	
	public void writeDocument(String xml) throws IOException
	{
		if ( xml == null ) return;
		
		String delimiter = createDelimeter(xml);
		
		out.write(delimiter);
		out.write(xml);
		out.write(delimiter);
		out.write("\n");
	}
	
	public void close() throws IOException
	{
		writeDocument(SmallDocumentReader.EOF_DOCUMENT);
		out.close();
	}
	
	private String createDelimeter(String xml)
	{
		while(true)
		{
			String proposed_delimiter = String.format("<?%s?>", random_data.randomStringOfLength(RandomData.ALPHABET_ALPHA_NUMERIC, 7));
			
			if ( xml == null || xml.indexOf(proposed_delimiter) == -1 )
				return proposed_delimiter;
		}
	}
	
}
