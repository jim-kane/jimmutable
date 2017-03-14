package org.kane.base.serialization.reader;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;

public class ObjectReader 
{
	private ReadTree t;
	
	private ObjectReader(ReadTree t)
	{
		this.t = t;
	}
	
	static public Object readDocument(String document, Object default_value)
	{
		ReadTree t = Parser.parse(document, null);
		if ( t == null ) return default_value;
		
		ObjectReader r = new ObjectReader(t);
		
		return r.asObject(default_value);
	}
	
	public Object asObject(Object default_value)
	{
		if ( !t.hasTypeHint() ) return default_value;
		
		if ( t.isPrimativeObject() )
		{
			ReadTree primative_value = t.find(FieldName.FIELD_NAME_PRIMATIVE_VALUE, null);
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_NULL) ) 
			{
				return default_value;
			}
			
			if ( primative_value == null ) return default_value;
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_STRING) )
			{
				return primative_value.getOptionalValueAsString(""); // if no primative value is present for a string, read it as the empty string... 
			}
			
			if ( !primative_value.hasValue()  ) return default_value; // If no primitive value, error for any other type.
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_CHAR) )
			{
				Character ret = primative_value.getOptionalValueAsCharacter(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_BOOLEAN) )
			{
				Boolean ret = primative_value.getOptionalValueAsBoolean(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_BYTE) )
			{
				Byte ret = primative_value.getOptionalValueAsByte(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_SHORT) )
			{
				Short ret = primative_value.getOptionalValueAsShort(null);
				if ( ret == null ) return default_value;
				return ret;
			} 
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_INT) )
			{
				Integer ret = primative_value.getOptionalValueAsInteger(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_LONG) )
			{
				Long ret = primative_value.getOptionalValueAsLong(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_FLOAT) )
			{
				Float ret = primative_value.getOptionalValueAsFloat(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			if ( t.isTypeHint(TypeName.TYPE_NAME_DOUBLE) )
			{
				Double ret = primative_value.getOptionalValueAsDouble(null);
				if ( ret == null ) return default_value;
				return ret;
			}
			
			return default_value; // unknown primative type? (should not be possible)
		}
		
		// Standard object converter...
		return default_value;
	}
}
