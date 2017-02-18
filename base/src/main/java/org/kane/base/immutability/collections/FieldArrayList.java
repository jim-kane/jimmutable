package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kane.base.immutability.StandardImmutableObject;

/**
 * An implementation of a Field object backed by an ArrayList. Generally
 * speaking, this Field will have all of the characteristics of an ArrayList
 * object that can be made immutable by calling freeze()
 * 
 * Note: ArrayList, and consequently FieldArrayList is not thread safe. This is
 * *generally* not a concern once "frozen" but if you a construction process
 * that is multi-threaded you should use FieldCopyOnWriteArrayList (warning --
 * slow construction)
 * 
 * In the "standard" case (construction in one thread) don't worry about any of
 * this and just use FieldArrayList.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
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
