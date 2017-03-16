package org.kane.base.serialization.writer;

import java.util.Map;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;
import org.kane.base.serialization.Validator;

abstract public class WriteAs 
{
	static public final WriteAs NATURAL_PRIMATIVES = new WriteAsNatural();
	static public final WriteAs OBJECTS = new WriteAsObject(); 
	
	
	abstract public void writeObject(ObjectWriter writer, FieldName field_name, Object obj);
	
	static private class WriteAsNatural extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( obj instanceof String ) { writer.writeString(field_name, (String)obj); return; }
			if ( obj instanceof Character ) { writer.writeChar(field_name, (Character)obj); return; }
			if ( obj instanceof Byte ) { writer.writeByte(field_name, (Byte)obj); return; }
			if ( obj instanceof Short ) { writer.writeShort(field_name, (Short)obj); return; }
			if ( obj instanceof Integer ) { writer.writeInt(field_name, (Integer)obj); return; }
			if ( obj instanceof Long ) { writer.writeLong(field_name, (Long)obj); return; }
			if ( obj instanceof Float ) { writer.writeFloat(field_name, (Float)obj); return; }
			if ( obj instanceof Double ) { writer.writeDouble(field_name, (Double)obj); return; }
			
			writer.writeObject(field_name, obj);
		}
	}
	
	static private class WriteAsObject extends WriteAs
	{
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			writer.writeObject(field_name, obj);
		}
	}
	
	static public class MapWriteAs extends WriteAs
	{
		private WriteAs key_as;
		private WriteAs value_as;
		
		public MapWriteAs(WriteAs key_as, WriteAs value_as)
		{
			Validator.notNull(key_as, value_as);
			
			this.key_as = key_as;
			this.value_as = value_as;
		}
		
		public void writeObject(ObjectWriter writer, FieldName field_name, Object obj)
		{
			if ( !(obj instanceof Map.Entry) ) return;
			
			writer.startObject(field_name, TypeName.TYPE_NAME_MAP_ENTRY);
			{
				Map.Entry entry = (Map.Entry)obj;
				
				key_as.writeObject(writer, FieldName.FIELD_KEY, entry.getKey());
				value_as.writeObject(writer, FieldName.FIELD_VALUE, entry.getValue());
			
				writer.endObject();
			}
		}
	}
	
}
