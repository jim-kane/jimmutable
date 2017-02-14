package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.TreeSet;

import org.kane.base.immutability.StandardImmutableObject;

public class FieldTreeSet<T> extends FieldSet<T>
{
	public FieldTreeSet()
	{
		super();
	}
	
	public FieldTreeSet(Iterable<T> objs)
	{
		super(objs);
	}
	
	protected Collection<T> createNewMutableInstance()
	{
		return new TreeSet();
	}

}
