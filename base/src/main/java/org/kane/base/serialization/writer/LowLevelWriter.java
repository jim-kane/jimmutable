package org.kane.base.serialization.writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Base64;

import javax.xml.namespace.QName;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.SerializeException;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;
import org.kane.base.serialization.reader.ObjectReader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class LowLevelWriter 
{
	private Format format;  // required, the format being written,
	private JsonGenerator gen; // required, the JSON geneator
	
	public LowLevelWriter(Format format, OutputStream out)
	{
		this(format, new OutputStreamWriter(out));
	}
	
	public LowLevelWriter(Format format, Writer writer)
	{
		Validator.notNull(format);
		
		this.format = format;
		
		try
		{
			if ( format == Format.JSON || format == Format.JSON_PRETTY_PRINT )
			{
				JsonFactory jfactory = new JsonFactory();
				gen = jfactory.createGenerator(writer);
				
				if ( format == Format.JSON_PRETTY_PRINT )
					gen.useDefaultPrettyPrinter();
			}
			else
			{
				XmlFactory xfactory = new XmlFactory();
				ToXmlGenerator xgen = xfactory.createGenerator(writer);

				if ( format == Format.XML_PRETTY_PRINT )
					xgen.useDefaultPrettyPrinter();
				
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
	
	public void writeFieldName(FieldName field_name)
	{
		try
		{
			Validator.notNull(field_name);
			
			if ( field_name.equals(FieldName.FIELD_ARRAY_ELEMENT) ) 
				return; // This is an array element, do not write the field name
			
			gen.writeFieldName(field_name.getSimpleName());
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public void writeNull() 
	{
		try
		{
			if ( isXML() )
			{
				// in XML, we should basically never get there (as nulls are simply "omitted" from the output).  
				// But, if some weird way, we wind up here, we need to explicitly write the null object out...
				
				gen.writeStartObject();
				
				writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
				gen.writeString(TypeName.TYPE_NAME_NULL.getSimpleName());
				
				writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
				gen.writeNull();
				
				gen.writeEndObject();
			}
			else
			{
				gen.writeNull();
			}
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	
	public void writeString(String str) 
	{
		if ( str == null )
		{
			writeNull();
			return;
		}
		
		if ( isXML() && str.length() == 0 )
		{
			writeStringObject(str);
			return;
		}
		
		try
		{
			gen.writeString(str);
		}
		catch(Exception e)
		{
			throw new SerializeException("Serialization error", e);
		}
	}
	
	public boolean isBase64Required(String src)
	{
		if ( src == null ) return false;
		
		char chars[] = src.toCharArray();
		
		boolean needs_strip = false;
		
		for ( char ch : chars )
		{
			if ( ch >= 32 && ch <= 10_000 ) continue;
			if ( ch == 9 ) continue; // tab
			if ( ch == 10 ) continue; // newline
			if ( ch == 13 ) continue; // carriage return;
			
			
			return true;
		}
		
		return false;
	}
	
	public String base64EncodeString(String str)
	{
		return Base64.getEncoder().encodeToString(str.getBytes());
	}
	
	public void writeBoolean(boolean value)
	{
		try
		{
			gen.writeBoolean(value);
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
	
	public void writeByte(byte value) 
	{
		writeLong(value);
	}

	public void writeShort(short value) 
	{ 
		writeLong(value); 
	}
	
	public void writeInt(int value) 
	{ 
		writeLong((long)value); 
	}
	
	public void writeLong(long value)
	{
		try
		{
			gen.writeNumber(value);
		}
		catch(Exception e)
		{
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
		}
	}
	
	public void writeStringObject(String str)
	{
		try
		{
			gen.writeStartObject();
			
			writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
			gen.writeString(TypeName.TYPE_NAME_STRING.getSimpleName());
			
			if ( str != null && isBase64Required(str) )
			{
				writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE_BASE64);
				gen.writeString(this.base64EncodeString(str));
			}
			else
			{
				writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
				
				if ( str == null ) 
					gen.writeNull();
				else
					gen.writeString(str);
			}
			
			gen.writeEndObject();
		}
		catch(Exception e)
		{
			throw new SerializeException("Low level write serialization error", e);
		}
	}
	
	public void writeObject(Object obj)
	{
		try
		{
			if ( obj == null )
			{
				writeNull();
				return;
			}
			
			if ( obj instanceof String )
			{
				String value = (String)obj;
				writeStringObject(value);
				return;
			}
			
			gen.writeStartObject();
			{
				if ( obj instanceof StandardWritable )
				{
					StandardWritable std = (StandardWritable)obj;
					
					TypeName type_name = std.getTypeName();
					
					if ( !ObjectReader.isTypeRegistered(type_name) )
					{
						System.err.println(String.format("WARNING! An object, %s, with type name %s is being written, but it is not registered with ObjectReader (meaning you will not be able to read/clone it)", 
								obj.getClass().getSimpleName(), 
								type_name.getSimpleName()));
						
						System.err.println("Register the class by calling ObjectReader.registerType");
					}
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(type_name.getSimpleName());
					
					std.write(new ObjectWriter(this));
				}
				
				else if ( obj instanceof Boolean )
				{
					Boolean value = (Boolean)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_BOOLEAN.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeBoolean(value.booleanValue());
				}
				
				else if ( obj instanceof Character )
				{
					Character value = (Character)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_CHAR.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeString(value.toString());
				}
				
				else if ( obj instanceof Byte )
				{
					Byte value = (Byte)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_BYTE.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeByte(value.byteValue());
				}
				
				else if ( obj instanceof Short )
				{
					Short value = (Short)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_SHORT.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeInt(value.shortValue());
				}
				
				else if ( obj instanceof Integer )
				{
					Integer value = (Integer)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_INT.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeInt(value.intValue());
				}
				
				else if ( obj instanceof Long )
				{
					Long value = (Long)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_LONG.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeLong(value.longValue());
				}
				
				else if ( obj instanceof Float )
				{
					Float value = (Float)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_FLOAT.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeFloat(value.floatValue());
				}
				
				else if ( obj instanceof Double )
				{
					Double value = (Double)obj;
					
					writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
					writeString(TypeName.TYPE_NAME_DOUBLE.getSimpleName());
					
					writeFieldName(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
					writeDouble(value.doubleValue());
				}
				
				else
				{
					throw new SerializeException(String.format("Attempt to serialize unknown type %s",obj.getClass()));
				}
				
				gen.writeEndObject();
			}
		}
		catch(SerializeException e )
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
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
			throw new SerializeException("Low level write serialization error", e);
		}
	}
	
	public void flush()
	{
		try
		{
			gen.flush();
		}
		catch(Exception e)
		{
			throw new SerializeException("Low level write serialization error", e);
		}
	}
	
	public void close()
	{
		try
		{
			gen.close();
		}
		catch(Exception e)
		{
			throw new SerializeException("Low level write serialization error", e);
		}
	}
	
	public Format getSimpleFormat() { return format; }
	public boolean isJSON() {  return getSimpleFormat() == Format.JSON || getSimpleFormat() == Format.JSON_PRETTY_PRINT; }
	public boolean isXML() { return getSimpleFormat() == Format.XML || getSimpleFormat() == Format.XML_PRETTY_PRINT; }
	
}
