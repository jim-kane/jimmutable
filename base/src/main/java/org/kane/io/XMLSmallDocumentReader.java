package org.kane.io;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

public class XMLSmallDocumentReader
{
	  private PushbackReader reader;
	  
	  public XMLSmallDocumentReader(Reader r)
	  {
		  reader = new PushbackReader(r);
	  }
	  
	  public String readUntil(char marker[], int read_limit, String default_value) throws IOException
	  {
		  StringBuilder ret = new StringBuilder();
		  
		  while(true)
		  {
			  if ( ret.length() > read_limit ) return default_value;
			  
			  if ( endsWith(ret,marker) )
			  {
				  try
				  {
					  reader.unread(marker);
					  
					  return ret.substring(0, ret.length()-marker.length);
				  }
				  catch(IOException e)
				  {
					  e.printStackTrace();
					  return default_value;
				  }
			  }
			  
			  int char_code = reader.read();
			  
			  if ( char_code == -1 ) // eof
				  return default_value;
			  
			  ret.append((char)char_code);
		  }
	  }
	 
	  
	  static private boolean endsWith(StringBuilder builder, char marker[])
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
	  
	  private void eat(char marker[]) throws IOException
	  {
		  for ( char ch : marker )
		  {
			  if ( ch != reader.read() ) 
				  throw new IOException("Eat failure: "+new String(marker));
		  }
	  }
	  
	  static public void main(String args[])
	  {
		 
	  }
}
