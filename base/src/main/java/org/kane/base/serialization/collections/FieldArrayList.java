package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kane.base.serialization.StandardImmutableObject;

public class FieldArrayList<T> extends AbstractFieldList<T>
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
