package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;

public class FieldArrayList<T> extends FieldList<T>
{
	public FieldArrayList()
	{
		super();
	}
	
	public FieldArrayList(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public FieldArrayList(StandardImmutableObject parent, Iterable<T> objs)
	{
		super(parent,objs);
	}


	protected List createNewMutableListInstance() 
	{
		return new ArrayList();
	}

	
	protected List createFieldSubList(StandardImmutableObject parent, Collection<T> objs) 
	{
		return new FieldArrayList(parent,objs);
	}
	
}
