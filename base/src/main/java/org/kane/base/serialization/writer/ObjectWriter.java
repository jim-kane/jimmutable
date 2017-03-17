package org.kane.base.serialization.writer;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.kane.base.exceptions.SerializeException;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.Format;
import org.kane.base.serialization.TypeName;
import org.kane.base.utils.Validator;

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
		
		if ( writer.isBase64Required(value) )
		{
			writer.writeStringObject(value);
		}
		else
		{
			writer.writeString(value);
		}
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
	
	public void startObject(FieldName field_name, TypeName type_name)
	{
		Validator.notNull(field_name, type_name);
		
		writer.writeFieldName(field_name);
		
		writer.openObject();
		writer.writeFieldName(FieldName.FIELD_NAME_TYPE_HINT);
		writer.writeString(type_name.getSimpleName());
	}
	
	public void endObject()
	{
		writer.closeObject();
	}
	
	public void writeCollection(FieldName field_name, Collection c, WriteAs write_as)
	{
		Validator.notNull(field_name, c, write_as);

		
		writer.writeFieldName(field_name);
		writer.openArray();
		
		for ( Object obj : c )
		{
			write_as.writeObject(this, FieldName.FIELD_ARRAY_ELEMENT, obj);
		}
		
		writer.closeArray();
	}
	
	public void writeMap(FieldName field_name, Map m, WriteAs write_keys_as, WriteAs write_values_as)
	{
		Validator.notNull(field_name, m, write_keys_as, write_values_as);
		
		writeCollection(field_name, m.entrySet(), new WriteAs.MapWriteAs(write_keys_as, write_values_as));
	}
	
	public Format getSimpleFormat() { return writer.getSimpleFormat(); }
	public boolean isJSON() { return writer.isJSON(); }
	public boolean isXML() { return writer.isXML(); }
	
	static public String serialize(Format format, Object obj)
	{
		try
		{
			if ( obj == null )
			{
				obj = NullPrimative.NULL_PRIMATIVE;
			}
			
			StringWriter writer = new StringWriter();
			LowLevelWriter low_level_writer = new LowLevelWriter(format,writer);
			
			if ( obj instanceof String )
			{
				low_level_writer.writeStringObject((String)obj);
			}
			else
			{
				low_level_writer.writeObject(obj);
			}
			
			low_level_writer.close();
			
			writer.close();
			
			return writer.toString();
		}
		catch(SerializeException e)
		{
			throw e;
		}
		catch(Exception e2)
		{
			throw new SerializeException("Error while writing object: "+e2.getMessage(), e2);
		}
	}
}
