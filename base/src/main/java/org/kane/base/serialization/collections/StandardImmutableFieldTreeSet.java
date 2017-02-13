package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.TreeSet;

import org.kane.base.serialization.StandardImmutableObject;

public class StandardImmutableFieldTreeSet<T> extends StandardImmutableFieldSet<T>
{
	public StandardImmutableFieldTreeSet()
	{
		super();
	}
	
	public StandardImmutableFieldTreeSet(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public StandardImmutableFieldTreeSet(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}
	
	protected Collection<T> createNewMutableInstance()
	{
		return new TreeSet();
	}

}
