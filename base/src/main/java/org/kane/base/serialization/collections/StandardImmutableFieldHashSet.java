package org.kane.base.serialization.collections;

import java.util.HashSet;
import java.util.Set;

import org.kane.base.serialization.StandardImmutableObject;

public class StandardImmutableFieldHashSet<T> extends StandardImmutableFieldSet<T>
{
	public StandardImmutableFieldHashSet()
	{
		super();
	}
	
	public StandardImmutableFieldHashSet(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public StandardImmutableFieldHashSet(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}
	
	protected Set<T> createNewMutableSetInstance()
	{
		return new HashSet();
	}

}
