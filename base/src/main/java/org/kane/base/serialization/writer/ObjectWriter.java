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
		writer.writeFieldName(field_name);
		writer.writeString(value);
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
	
	
	public void writeExplicitlyTypedString(FieldName field_name, String value)
	{
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedString(value);
	}
	
	public void writeExplicitlyTypedByte(FieldName field_name, byte value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedByte(value);
	}
	
	public void writeExplicitlyTypedShort(FieldName field_name, short value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedShort(value);
	}
	
	public void writeExplicitlyTypedInt(FieldName field_name, int value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedInt(value);
	}
	
	public void writeExplicitlyTypedLong(FieldName field_name, long value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplicitlyTypedLong(value);
	}
	
	public void writeExplicitlyTypedFloat(FieldName field_name, float value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplitlyTypedFloat(value);
	}
	
	public void writeExplicitlyTypedDouble(FieldName field_name, double value) 
	{
		writer.writeFieldName(field_name);
		writer.writeExplitlyTypedDouble(value);
	}
	
	public void writeObject(FieldName field_name, StandardWritable obj)
	{
		writer.writeFieldName(field_name);
		
		writer.openObject();
		{
			obj.write(this);
			writer.closeObject();
		}
	}
	
	public void writeExplictlyTypedObject(FieldName field_name, StandardWritable obj)
	{
		writer.writeFieldName(field_name);
		
		writer.openObject(obj.getTypeName());
		{
			obj.write(this);
			writer.closeObject();
		}
	}
	
	public ArrayWriter writeOpenArray(FieldName field_name)
	{
		writer.writeFieldName(field_name);
		writer.openArray();
		
		return new ArrayWriter(writer);
	}
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return writer.isJSON(); }
	public boolean isXML() { return writer.isXML(); }
}
