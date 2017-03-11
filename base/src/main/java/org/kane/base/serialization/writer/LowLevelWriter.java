package org.kane.base.serialization.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.namespace.QName;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.SerializeException;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class LowLevelWriter 
{
	private Format format; // required
	
	private JsonGenerator gen;
	
	public LowLevelWriter(Format format, Writer writer)
	{
		Validator.notNull(format);
		
		this.format = format;
		
		try
		{
			if ( format == Format.JSON )
			{
				JsonFactory jfactory = new JsonFactory();
				gen = jfactory.createGenerator(writer);
			}
			else
			{
				XmlFactory xfactory = new XmlFactory();
				ToXmlGenerator xgen = xfactory.createGenerator(writer);
				
				xgen.writeRaw("<?xml version='1.0' encoding='UTF-8'?>");
				xgen.setNextName(new QName("object"));
				
				gen = xgen;
			}
		}
		catch(Exception e)
		{
			throw new SerializeException("Error creating low level writer", e);
		}
	}
	
	public LowLevelWriter(Format format, OutputStream out)
	{
		this(format, new OutputStreamWriter(out));
	}
	
	public void beginDocument(TypeName type) throws IOException
	{
		try
		{
			Validator.notNull(type);
			
			gen.writeStartObject();
			gen.writeStringField(FieldName.FIELD_NAME_TYPE_HINT.getSimpleName(), type.getSimpleName());
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void endDocument() throws IOException
	{
		gen.writeEndObject();
		gen.close();
	}
	
	public void writeFieldName(FieldName field_name)
	{
		try
		{
			Validator.notNull(field_name);
			gen.writeFieldName(field_name.getSimpleName());
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void openObject() 
	{
		try
		{
			gen.writeStartObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void openObject(TypeName explicit_type)
	{
		try
		{
			Validator.notNull(explicit_type);
			
			gen.writeStartObject();

			gen.writeFieldName("TYPE");
			gen.writeString(explicit_type.getSimpleName());
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void closeObject() 
	{
		try
		{
			gen.writeEndObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeString(String str) 
	{
		try
		{
			gen.writeString(str);
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeExplicitlyTypedString(String str) 
	{
		try
		{
			openObject(TypeName.TYPE_NAME_STRING);
			writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
			writeString(str);
			closeObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeChar(char value) 
	{ 
		writeString(String.format("%c", value)); 
	}
	
	public void writeExplicitlyTypedChar(char value) 
	{ 
		try
		{
			openObject(TypeName.TYPE_NAME_CHAR);
			writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
			writeString(String.format("%c", value));
			closeObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	
	
	public void writeByte(byte value) 
	{
		writeLong(value);
	}
	
	public void writeExplicitlyTypedByte(byte value) 
	{ 
		writeExplitlyTypedNumber(TypeName.TYPE_NAME_BYTE, value);
	}
	
	public void writeShort(short value) 
	{ 
		writeLong(value); 
	}
	
	public void writeExplicitlyTypedShort(short value) 
	{ 
		writeExplitlyTypedNumber(TypeName.TYPE_NAME_SHORT, value);
	}
	
	public void writeInt(int value) 
	{ 
		writeLong((long)value); 
	}
	
	public void writeExplicitlyTypedInt(int value) 
	{ 
		writeExplitlyTypedNumber(TypeName.TYPE_NAME_INT, value);
	}
	
	public void writeLong(long value)
	{
		try
		{
			gen.writeNumber(value);
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeExplicitlyTypedLong(long value) 
	{ 
		writeExplitlyTypedNumber(TypeName.TYPE_NAME_LONG, value);
	}
	
	private void writeExplitlyTypedNumber(TypeName type, long value)
	{
		try
		{
			openObject(type);
			writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
			writeLong(value);
			closeObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeFloat(float value)
	{
		try
		{
			gen.writeNumber(value);
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeExplitlyTypedFloat(float value)
	{
		try
		{
			openObject(TypeName.TYPE_NAME_FLOAT);
			writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
			writeFloat(value);
			closeObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeDouble(double value)
	{
		try
		{
			gen.writeNumber(value);
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeExplitlyTypedDouble(double value)
	{
		try
		{
			openObject(TypeName.TYPE_NAME_DOUBLE);
			writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
			writeDouble(value);
			closeObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void openArray()
	{
		try
		{
			gen.writeStartArray();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void closeArray() 
	{
		try
		{
			gen.writeEndArray();
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	
	
	public Format getSimpleFormat() { return format; }
	public boolean isJSON() { return getSimpleFormat() == Format.JSON; }
	public boolean isXML() { return getSimpleFormat() == Format.XML; }
	
}
