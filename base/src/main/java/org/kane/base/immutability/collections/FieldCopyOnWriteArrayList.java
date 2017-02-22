package org.kane.base.immutability.collections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An implementation of a Field object backed by an FieldCopyOnWriteArrayList.
 * Generally speaking, this Field will have all of the characteristics of a
 * FieldCopyOnWriteArrayList object that can be made immutable by calling
 * freeze()
 * 
 * Why does one need a thread safe immutable list? Well... if your
 * *construction* code (when the object is mutable) is threaded then it is *much
 * safer* to use this list afs, the java language being what it is, it is neigh
 * on impossible to guarantee the mutable -> immutable transition in this
 * multi-threaded context absent a concurrent backing store.
 * 
 * In general, however (when the field is being created in single threaded code)
 * don't worry about a thing and just use FieldArrayList, which is also *far*
 * faster to construct.
 * 
 * @author jim.kane
 *
 * @param <T>
 */

public class FieldCopyOnWriteArrayList<T> extends FieldList<T>
{
	public FieldCopyOnWriteArrayList()
	{
		super();
	}
	
	
	public FieldCopyOnWriteArrayList(Iterable<T> objs)
	{
		super(objs);
	}

	protected List createNewMutableListInstance() 
	{
		return new CopyOnWriteArrayList();
	}
	
	
}

