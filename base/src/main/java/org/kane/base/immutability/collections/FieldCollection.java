package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

/**
 * An implementation of a collection that begins life as immutable but can, at
 * any time, be "frozen" (made immutable) by calling the freeze method. In other
 * words, a Collection class that implements Field.
 * 
 * Do not fear extending this collection object as needed, such implementations
 * tend to go very quickly as the base class does nearly all of the work for
 * you. Notwithstanding the foregoing, would be extenders of this class should
 * take time to carefully understand the immutability principles involved and
 * write careful unit tests to make sure that their implementations are as
 * strictly immutable as possible.
 * 
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class FieldCollection<T> implements Collection<T>, Field
{
	transient private boolean is_frozen = true;
	
	private Collection<T> contents;
	
	abstract protected Collection<T> createNewMutableInstance();
	
	public FieldCollection()
	{
		is_frozen = false;
		contents = createNewMutableInstance();
	}

	public FieldCollection(Iterable<T> objs)
	{
		this();
		
		if ( objs == null ) objs = Collections.EMPTY_LIST;
		
		for ( T obj : objs )
		{
			add(obj);
		}
	}
	
	public void freeze() { is_frozen = true; }
	public boolean getSimpleIsFrozen()  { return is_frozen; }

	public int size() { return contents.size(); }
	public boolean isEmpty() { return contents.isEmpty(); }
	public boolean contains(Object o) { return contents.contains(o); }
	public Object[] toArray() { return contents.toArray(); }
	public <T> T[] toArray(T[] a) { return contents.toArray(a); }
	public boolean containsAll(Collection<?> c) { return contents.containsAll(c); }
	
	public boolean add(T e)
	{
		assertNotFrozen();
		return contents.add(e);
	}
	
	public boolean remove(Object o)
	{
		assertNotFrozen();
		return contents.remove(o);
	}
	
	public boolean addAll(Collection<? extends T> c)
	{
		assertNotFrozen();
		return contents.addAll(c);
	}

	
	public boolean retainAll(Collection<?> c)
	{
		assertNotFrozen();
		return contents.retainAll(c);
	}

	
	public boolean removeAll(Collection<?> c)
	{
		assertNotFrozen();
		return contents.removeAll(c);
	}

	
	public void clear()
	{
		assertNotFrozen();
		contents.clear();
	}
	
	public int hashCode() 
	{
		return contents.hashCode();
	}


	public boolean equals(Object obj) 
	{
		if (!(obj instanceof Set) ) return false;
		
		Set other = (Set)obj;
		
		if ( other.size() != size() ) return false;
		
		return other.containsAll(contents);
	}

	public String toString() 
	{
		return super.toString();
	}

	public Iterator<T> iterator()
	{
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<T>
	{
		private Iterator<T> itr;
		
		public MyIterator()
		{
			itr = contents.iterator();
		}

		
		public boolean hasNext()
		{
			return itr.hasNext();
		}

		public T next()
		{
			return itr.next();
		}

		public void remove()
		{
			assertNotFrozen();
			itr.remove();
		}
	}
}

