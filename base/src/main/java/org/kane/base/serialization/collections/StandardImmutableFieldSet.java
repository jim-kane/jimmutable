package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

/**
 * Provides a class that properly implements "selective mutability" for set
 * field types... See StandardImmutableFieldList for commentary on why this is
 * needed...
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class StandardImmutableFieldSet<T> extends StandardImmutableFieldCollection<T> implements Set<T>
{
	public StandardImmutableFieldSet()
	{
		super(null);
	}
	
	public StandardImmutableFieldSet(StandardImmutableObject parent)
	{
		super(parent);
		
	}
	
	public StandardImmutableFieldSet(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}
}
