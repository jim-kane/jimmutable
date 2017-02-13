package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

/**
 * Provides a class that properly implements "selective mutability" for set
 * field types... See StandardImmutableFieldList for commentary on why this is
 * needed...
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class FieldSet<T> extends FieldCollection<T> implements Set<T>
{
	public FieldSet()
	{
		super(null);
	}
	
	public FieldSet(StandardImmutableObject parent)
	{
		super(parent);
		
	}
	
	public FieldSet(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}
}
