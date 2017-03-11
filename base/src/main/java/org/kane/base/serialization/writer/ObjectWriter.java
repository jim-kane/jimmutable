package org.kane.base.serialization.writer;

import java.io.StringWriter;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Validator;

public class ObjectWriter 
{
	private LowLevelWriter writer;
	
	public ObjectWriter(LowLevelWriter writer)
	{
		this.writer = writer;
	}
	
	public void writeString(FieldName field_name, String value)
	{
		Validator.notNull(field_name);
		
		writer.writeFieldName(field_name);
		writer.writeString(value);
	}
	
	public void writeExplicitlyTypedString(FieldName field_name, String value)
	{
		Validator.notNull(field_name);
		
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedString(value);
	}
	
	
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
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return getSimpleFormat() == Format.JSON; }
	public boolean isXML() { return getSimpleFormat() == Format.XML; }
}
