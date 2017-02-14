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
	
	
	public FieldArrayList(Iterable<T> objs)
	{
		super(objs);
	}

	protected List createNewMutableListInstance() 
	{
		return new ArrayList();
	}
	
}
