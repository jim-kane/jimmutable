package org.kane.base.serialization.writer;

import org.kane.base.serialization.TypeName;

/**
 * An interface that must be implemented by all (non-primitive) objects that
 * wish to be writable by ObjectWriter.
 * 
 * Notably, StandardObject implements StandardWritable (meaning that
 * ObjectWriter can write any StandardObject)
 * 
 * @author jim.kane
 *
 */
public interface StandardWritable 
{
	public TypeName getTypeName();
	public void write(ObjectWriter writer);
}
