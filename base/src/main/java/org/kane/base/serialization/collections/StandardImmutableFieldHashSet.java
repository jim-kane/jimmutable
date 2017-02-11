package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

/**
 * This is a utility class that makes it easy to create a properly behaved hash
 * set field of an StandardImmutableObject.
 * 
 * What's hard about that? Well, you need to have a list that is mutable (when
 * the owning object is mutable) and then immutable when the owning object is
 * Immutable. Using Collection.unmodifiableSet is clunky...
 * StandardImmutableFieldHashSet takes care of the heavy lifting for you!
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public class StandardImmutableFieldHashSet<T> extends HashSet<T>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	transient private StandardImmutableObject parent;
	
	
	public StandardImmutableFieldHashSet(StandardImmutableObject parent)
	{
		super();
		this.parent = parent;
	}
	
	public StandardImmutableFieldHashSet(StandardImmutableObject parent, Collection<T> objs)
	{
		super();
		
		this.parent = parent;
		
		addAll(objs);
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
	
	public boolean addAll(Collection c) 
	{
		assertNotComplete();
		return super.addAll(c);
	}

	
	public boolean retainAll(Collection c) 
	{
		assertNotComplete();
		return super.retainAll(c);
	}

	
	public boolean removeAll(Collection c)
	{
		assertNotComplete();
		return super.removeAll(c);
	}


	public void clear() 
	{
		assertNotComplete();
		super.clear();
	}
}
