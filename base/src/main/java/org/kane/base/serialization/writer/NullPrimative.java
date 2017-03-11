package org.kane.base.serialization.writer;

import org.kane.base.serialization.FieldName;
import org.kane.base.serialization.TypeName;

public class NullPrimative implements StandardWritable 
{
	static public final NullPrimative NULL_PRIMATIVE = new NullPrimative();
	
	private NullPrimative() {}
	
	public TypeName getTypeName() 
	{
		return TypeName.TYPE_NAME_NULL;
	}

	public void write(ObjectWriter writer) 
	{
		writer.writeNull(FieldName.FIELD_NAME_PRIMATIVE_VALUE);
	}
	
}
