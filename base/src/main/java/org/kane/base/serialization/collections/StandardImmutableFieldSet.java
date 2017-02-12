package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

/**
 * Provides a class that properly implements "selective mutability" for set
 * field types... See StandardImmutableFieldList for commentary on why this is
 * needed...
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class StandardImmutableFieldSet<T> implements Set<T>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	transient private StandardImmutableObject parent;
	
	private Set<T> contents;
	
	abstract protected Set<T> createNewMutableSetInstance();
	
	public StandardImmutableFieldSet()
	{
		this(null);
		
	}
	
	public StandardImmutableFieldSet(StandardImmutableObject parent)
	{
		this.parent = parent;
		contents = createNewMutableSetInstance();
	}
	
	public StandardImmutableFieldSet(StandardImmutableObject parent, Iterable<T> objs)
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
		if ( parent == null ) // an unset parent can only occour when this object was created via xstream de-serialization... Therefore, the object is not mutable...
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
