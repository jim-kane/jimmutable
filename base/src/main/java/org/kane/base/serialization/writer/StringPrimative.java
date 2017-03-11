package org.kane.base.serialization.writer;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;

public class StringPrimative implements StandardWritable 
{
	private String value;
	
	public StringPrimative(String value)
	{
		this.value = value;
	}
	
	public TypeName getTypeName() 
	{
		return TypeName.TYPE_NAME_STRING;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeString(FieldName.FIELD_NAME_PRIMATIVE_VALUE, value);
	}
}
