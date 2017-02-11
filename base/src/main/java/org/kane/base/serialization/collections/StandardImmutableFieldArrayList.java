package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

public class StandardImmutableFieldArrayList<T> extends ArrayList<T>
{
	transient private StandardImmutableObject parent;
	
	public StandardImmutableFieldArrayList(StandardImmutableObject parent)
	{
		super();
		this.parent = parent;
	}
	
	public StandardImmutableFieldArrayList(StandardImmutableObject parent, Collection<T> objs)
	{
		super();
		
		this.parent = parent;
		
		addAll(objs);
	}
	
	public StandardImmutableFieldArrayList(StandardImmutableObject parent, T objs[])
	{
		super();
		
		this.parent = parent;
		
		for ( T obj : objs )
			add(obj);
	}
	
	private void assertNotComplete()
	{
		if ( parent == null ) // an unset parent can only occour when this object was created via xstream de-serialization... Therfore, the object is not mutable...
			throw new ImmutableException();
		else 
			parent.assertNotComplete();
	}
	
	public boolean add(T e) 
	{
		assertNotComplete();
		return super.add(e);
	}

	public boolean remove(Object o) 
	{
		assertNotComplete();
		return super.remove(o);
	}

	public boolean addAll(Collection<? extends T> c) 
	{
		assertNotComplete();
		return super.addAll(c);
	}

	
	public boolean addAll(int index, Collection<? extends T> c) 
	{
		assertNotComplete();
		return super.addAll(index,c);
	}

	
	public boolean removeAll(Collection<?> c) 
	{
		assertNotComplete();
		return super.removeAll(c);
	}

	
	public boolean retainAll(Collection<?> c) 
	{
		assertNotComplete();
		return super.retainAll(c);
	}

	
	public void clear() 
	{
		assertNotComplete();
		super.clear();
	}

	
	public T set(int index, T element) 
	{
		assertNotComplete();
		return super.set(index, element);
	}

	
	public void add(int index, T element) 
	{
		assertNotComplete();
		super.add(index,element);
	}

	
	public T remove(int index) 
	{
		assertNotComplete();
		return super.remove(index);
	}
	
	
}
