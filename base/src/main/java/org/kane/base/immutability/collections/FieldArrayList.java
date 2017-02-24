package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.List;

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
 * @param <E>
 */
public class FieldArrayList<E> extends FieldList<E>
{
	public FieldArrayList()
	{
		super();
	}
	
	public FieldArrayList(Iterable<E> objs)
	{
		super(objs);
	}

	@Override
	protected List<E> createNewMutableInstance() 
	{
		return new ArrayList<>();
	}
}
