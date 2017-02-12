package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kane.base.serialization.StandardImmutableObject;

public class StandardImmutableFieldArrayList<T> extends StandardImmutableFieldList<T>
{
	public StandardImmutableFieldArrayList()
	{
		super();
	}
	
	public StandardImmutableFieldArrayList(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public StandardImmutableFieldArrayList(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}


	protected List createNewMutableListInstance() 
	{
		return new ArrayList();
	}

	
	protected List createFieldSubList(StandardImmutableObject parent, Collection<T> objs) 
	{
		return new StandardImmutableFieldArrayList(parent,objs);
	}
	
}
