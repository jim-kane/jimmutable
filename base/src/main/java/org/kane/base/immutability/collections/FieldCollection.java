package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

abstract public class FieldCollection<T> implements Collection<T>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	private StandardImmutableObject parent;
	
	private Collection<T> contents;
	
	abstract protected Collection<T> createNewMutableInstance();
	
	public FieldCollection()
	{
		this(null);
		
	}
	
	public FieldCollection(StandardImmutableObject parent)
	{
		this.parent = parent;
		contents = createNewMutableInstance();
	}
	
	public FieldCollection(StandardImmutableObject parent, Iterable<T> objs)
	{
		this(parent);
		
		if ( objs != null )
		{
			for ( T obj : objs )
			{
				add(obj);
			}
		}
	}
	
	public void assertNotComplete()
	{
		if ( parent == null ) // this should never happen, but... should it take place... default to immutable...
			throw new ImmutableException();
		else 
			parent.assertNotComplete();
	}

	public int size() { return contents.size(); }
	public boolean isEmpty() { return contents.isEmpty(); }
	public boolean contains(Object o) { return contents.contains(o); }
	public Object[] toArray() { return contents.toArray(); }
	public <T> T[] toArray(T[] a) { return contents.toArray(a); }
	public boolean containsAll(Collection<?> c) { return contents.containsAll(c); }
	
	public boolean add(T e)
	{
		assertNotComplete();
		return contents.add(e);
	}
	
	public boolean remove(Object o)
	{
		assertNotComplete();
		return contents.remove(o);
	}
	
	public boolean addAll(Collection<? extends T> c)
	{
		assertNotComplete();
		return contents.addAll(c);
	}

	
	public boolean retainAll(Collection<?> c)
	{
		assertNotComplete();
		return contents.retainAll(c);
	}

	
	public boolean removeAll(Collection<?> c)
	{
		assertNotComplete();
		return contents.removeAll(c);
	}

	
	public void clear()
	{
		assertNotComplete();
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
			assertNotComplete();
			itr.remove();
		}
	}
}

