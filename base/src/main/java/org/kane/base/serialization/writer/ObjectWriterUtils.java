package org.kane.base.serialization.writer;

import java.io.StringWriter;

import org.kane.base.serialization.Validator;

public class ObjectWriterUtils 
{
	static public String writeObject(Format format, Object obj, String default_value)
	{
		try
		{
			if ( obj == null )
			{
				obj = NullPrimative.NULL_PRIMATIVE;
			}
			
			if ( obj instanceof String )
			{
				obj = new StringPrimative((String)obj);
			}
			
			StringWriter writer = new StringWriter();
			LowLevelWriter low_level_writer = new LowLevelWriter(format,writer);
			
			low_level_writer.writeObject(obj);
			low_level_writer.close();
			
			writer.close();
			
			return writer.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return default_value;
		}
	}
}
