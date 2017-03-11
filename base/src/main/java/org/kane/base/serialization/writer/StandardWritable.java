package org.kane.base.serialization.writer;

import org.kane.base.serialization.TypeName;

public interface StandardWritable 
{
	public TypeName getTypeName();
	public void write(ObjectWriter writer);
}
