package org.kane.base.serialization.writer;

import java.util.Map;

import org.kane.base.exceptions.SerializeException;
import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.StandardObject;
import org.kane.base.serialization.TypeName;
import org.kane.base.utils.Validator;

abstract public class WriteAs 
{
	static public final WriteAs OBJECT = new WriteAsObject();
	static public final WriteAs STRING = new WriteAsString();
	static public final WriteAs NUMBER = new WriteAsNumber();
	static public final WriteAs BOOLEAN = new WriteAsBoolean();

	abstract public void writeObject(ObjectWriter writer, FieldName field_name, Object obj);
	
	static private class WriteAsObject extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			writer.writeObject(field_name, obj);
		}
	}
	
	static private class WriteAsString extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( obj == null ) 
			{
				writer.writeNull(field_name);
				return;
			}
			
			if ( obj instanceof StandardObject ) throw new SerializeException("Attempt to write a standard object as a string");
			if ( obj instanceof Map.Entry ) throw new SerializeException("Attempt to write a map entry as a string");
			
			writer.writeString(field_name, obj.toString());
		}
	}
	
	static private class WriteAsNumber extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( obj == null ) throw new SerializeException("Attempt to write null as a number");
			
			if ( obj instanceof Boolean ) { writer.writeInt(field_name, ((Boolean)obj).booleanValue() ? 1 : 0); return; }
			if ( obj instanceof Character ) { writer.writeInt(field_name, (int)((Character)obj).charValue()); return; }
			if ( obj instanceof Byte ) { writer.writeByte(field_name, (Byte)obj); return; }
			if ( obj instanceof Short ) { writer.writeShort(field_name, (Short)obj); return; }
			if ( obj instanceof Integer ) { writer.writeInt(field_name, (Integer)obj); return; }
			if ( obj instanceof Long ) { writer.writeLong(field_name, (Long)obj); return; }
			if ( obj instanceof Float ) { writer.writeFloat(field_name, (Float)obj); return; }
			if ( obj instanceof Double ) { writer.writeDouble(field_name, (Double)obj); return; }
			
			throw new SerializeException(String.format("Unable to write %s as a number", obj.getClass().getSimpleName()));
		}
	}
	
	static private class WriteAsBoolean extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( obj == null ) { writer.writeBoolean(field_name, false); }
			
			if ( obj instanceof Boolean ) { writer.writeBoolean(field_name, (Boolean)obj); }
			
			if ( obj instanceof Byte ) { writer.writeBoolean(field_name, (Byte)obj != 0); }
			if ( obj instanceof Short ) { writer.writeBoolean(field_name, (Short)obj != 0); }
			if ( obj instanceof Integer ) { writer.writeBoolean(field_name, (Integer)obj != 0); }
			if ( obj instanceof Long ) { writer.writeBoolean(field_name, (Long)obj != 0); }
			
			
			throw new SerializeException(String.format("Unable to write %s as a boolean", obj.getClass().getSimpleName()));
		}
	}
	
	static public class MapWriteAs extends WriteAs
	{
		private WriteAs key_type;
		private WriteAs value_type;
		
		public MapWriteAs(WriteAs key_type, WriteAs value_type)
		{
			Validator.notNull(key_type, value_type);
			
			this.key_type = key_type;
			this.value_type = value_type;
		}
		
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( !(obj instanceof Map.Entry) ) return;
			
			writer.startObject(field_name, TypeName.TYPE_NAME_MAP_ENTRY);
			{
				Map.Entry entry = (Map.Entry)obj;
				
				key_type.writeObject(writer, FieldName.FIELD_KEY, entry.getKey());
				value_type.writeObject(writer, FieldName.FIELD_VALUE, entry.getValue());
			
				writer.endObject();
			}
		}
	}
	
}
