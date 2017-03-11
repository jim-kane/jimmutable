package org.kane.base.serialization.writer;

import java.io.StringWriter;

import org.kane.base.serialization.Validator;

public class ObjectWriterUtils 
{
	static public String writeObject(Format format, StandardWritable obj, String default_value)
	{
		try
		{
			Validator.notNull(obj);
			
			StringWriter writer = new StringWriter();
			LowLevelWriter low_level_writer = new LowLevelWriter(format,writer);
			
			low_level_writer.beginDocument(obj.getTypeName());
			{
				obj.write(new ObjectWriter(low_level_writer));
			
				low_level_writer.endDocument();
			}
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
