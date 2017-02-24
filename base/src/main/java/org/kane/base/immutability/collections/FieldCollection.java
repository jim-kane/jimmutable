package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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
 * @param <E>
 */
abstract public class FieldCollection<E> implements Collection<E>, Field
{
	transient private boolean is_frozen = true;
	
	abstract protected Collection<E> getContents();
	
	public FieldCollection()
	{
		is_frozen = false;
	}

	public FieldCollection(Iterable<E> objs)
	{
		this();
		
		if ( objs == null ) objs = Collections.emptyList();
		
		for ( E obj : objs )
		{
			add(obj);
		}
	}
	
	public void freeze() { is_frozen = true; }
	public boolean getSimpleIsFrozen()  { return is_frozen; }

	public int size() { return getContents().size(); }
	public boolean isEmpty() { return getContents().isEmpty(); }
	public boolean contains(Object o) { return getContents().contains(o); }
	public Object[] toArray() { return getContents().toArray(); }
	public <T> T[] toArray(T[] a) { return getContents().toArray(a); }
	public boolean containsAll(Collection<?> c) { return getContents().containsAll(c); }
	
	public boolean add(E e)
	{
		assertNotFrozen();
		return getContents().add(e);
	}
	
	public boolean remove(Object o)
	{
		assertNotFrozen();
		return getContents().remove(o);
	}
	
	public boolean addAll(Collection<? extends E> c)
	{
		assertNotFrozen();
		return getContents().addAll(c);
	}
	
	public boolean retainAll(Collection<?> c)
	{
		assertNotFrozen();
		return getContents().retainAll(c);
	}
	
	public boolean removeAll(Collection<?> c)
	{
		assertNotFrozen();
		return getContents().removeAll(c);
	}
	
	public void clear()
	{
		assertNotFrozen();
		getContents().clear();
	}
	
	public int hashCode() 
	{
		return getContents().hashCode();
	}

	public boolean equals(Object obj) 
	{
		if (!(obj instanceof Collection) ) return false;
		
		@SuppressWarnings("unchecked")
		Collection<E> other = (Collection<E>)obj;
		
		if ( size() != other.size() ) return false;
		
		return containsAll(other);
	}

	public String toString() 
	{
		return super.toString();
	}

	public Iterator<E> iterator()
	{
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<E>
	{
		private Iterator<E> itr;
		
		public MyIterator()
		{
			itr = getContents().iterator();
		}

		public boolean hasNext()
		{
			return itr.hasNext();
		}

		public E next()
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

