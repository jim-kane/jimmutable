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
	
	public void writeNull(FieldName field_name)
	{
		if ( writer.isXML() ) return;  // in xml, a null is written by simply "not writing" the field...
		
		writer.writeFieldName(field_name);
		writer.writeNull();
	}
	
	public void writeString(FieldName field_name, String value)
	{
		if ( writer.isXML() && value == null ) return; // in xml, a null is written by simply "not writing" the field...
		
		writer.writeFieldName(field_name);
		writer.writeString(value);
	}
	
	public void writeBoolean(FieldName field_name, boolean value) 
	{
		writer.writeFieldName(field_name);
		writer.writeBoolean(value);
	}
	
	public void writeChar(FieldName field_name, char value) 
	{
		writer.writeFieldName(field_name);
		writer.writeChar(value);
	}
	
	public void writeByte(FieldName field_name, byte value) 
	{
		writer.writeFieldName(field_name);
		writer.writeByte(value);
	}
	
	public void writeShort(FieldName field_name, short value) 
	{
		writer.writeFieldName(field_name);
		writer.writeShort(value);
	}
	
	public void writeInt(FieldName field_name, int value) 
	{
		writer.writeFieldName(field_name);
		writer.writeInt(value);
	}
	
	public void writeLong(FieldName field_name, long value) 
	{
		writer.writeFieldName(field_name);
		writer.writeLong(value);
	}
	
	public void writeFloat(FieldName field_name, float value) 
	{
		writer.writeFieldName(field_name);
		writer.writeFloat(value);
	}
	
	public void writeDouble(FieldName field_name, double value) 
	{
		writer.writeFieldName(field_name);
		writer.writeDouble(value);
	}
	
	
	public void writeObject(FieldName field_name, Object value)
	{
		if ( writer.isXML() && value == null ) return; // in xml, a null is written by simply "not writing" the field...
		
		writer.writeFieldName(field_name);
		writer.writeObject(value);
	}
	
	public ArrayWriter openArray(FieldName field_name)
	{
		writer.writeFieldName(field_name);
		return writer.openArray();
	}
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return writer.isJSON(); }
	public boolean isXML() { return writer.isXML(); }
}
