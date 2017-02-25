package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of a map that begins life as immutable but can, at
 * any time, be "frozen" (made immutable) by calling the freeze method. In other
 * words, a Map class that implements Field.
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

abstract public class FieldMap<K,V> implements Map<K,V>, Field
{
	transient private boolean is_frozen = true;
	
	private Map<K,V> contents = createNewMutableInstance();
	
	abstract protected Map<K,V> createNewMutableInstance();
	
	
	public FieldMap()
	{
		is_frozen = false;
	}
	
	public FieldMap(Map<K,V> initial_values)
	{
		this();
		
		if ( initial_values != null )
			contents.putAll(initial_values);
	}
	
	public void freeze() { is_frozen = true; }
	public boolean getSimpleIsFrozen()  { return is_frozen; }
	
	public int size() { return contents.size(); }
	public boolean isEmpty() { return contents.isEmpty(); }
	public boolean containsKey(Object key) { return contents.containsKey(key); }
	public boolean containsValue(Object value) { return contents.containsValue(value); }
	public V get(Object key) { return contents.get(key); }

	
	public V put(K key, V value)
	{
		assertNotFrozen();
		return contents.put(key, value);
	}
	
	public V remove(Object key)
	{
		assertNotFrozen();
		return contents.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m)
	{
		assertNotFrozen();
		contents.putAll(m);
	}
	
	public void clear()
	{
		assertNotFrozen();
		contents.clear();
	}
	
	public Set<K> keySet()
	{
		return new InnerSet<>(contents.keySet());
	}

	
	public Collection<V> values()
	{
		return new InnerCollection<>(contents.values());
	}

	
	public Set<Map.Entry<K, V>> entrySet()
	{
		return new InnerSet<>(contents.entrySet());
	}

	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Map) ) return false;
		
		@SuppressWarnings("unchecked")
		Map<K, V> other = (Map<K, V>)obj;
		
		if ( size() != other.size() ) return false;
		
		return entrySet().containsAll(other.entrySet());
	}
	
	public String toString() 
	{
		return contents.toString();
	}
	
	private class InnerCollection<E> implements Collection<E>
	{
		private Collection<E> inner_contents;
		
		private InnerCollection(Collection<E> c)
		{
			inner_contents = c;
		}
		
		public int size() { return inner_contents.size(); }
		public boolean isEmpty() { return inner_contents.isEmpty(); }
		public boolean contains(Object o) { return inner_contents.contains(o); }
		
		public Iterator<E> iterator() 
		{
			return new InnerIterator<>(inner_contents.iterator());
		}
		
		public Object[] toArray() { return inner_contents.toArray(); }
		public <T> T[] toArray(T[] a) { return inner_contents.toArray(a); }

		public boolean add(E e)
		{
			assertNotFrozen();
			return inner_contents.add(e);
		}
		
		public boolean remove(Object o) 
		{
			assertNotFrozen();
			return inner_contents.remove(o);
		}

		
		public boolean containsAll(Collection<?> c) { return inner_contents.containsAll(c); }

		
		public boolean addAll(Collection<? extends E> c) 
		{
			assertNotFrozen();
			return inner_contents.addAll(c);
		}

		public boolean removeAll(Collection<?> c) 
		{
			assertNotFrozen();
			return inner_contents.removeAll(c);
		}

		
		public boolean retainAll(Collection<?> c) 
		{
			assertNotFrozen();
			return inner_contents.retainAll(c);
		}

		
		public void clear() 
		{
			assertNotFrozen();
			inner_contents.clear();
		}
	}
	
	private class InnerSet<E> extends InnerCollection<E> implements Set<E>
	{
		private InnerSet(Collection<E> c)
		{
			super(c);
		}
	}
	
	private class InnerIterator<E> implements Iterator<E>
	{
		private Iterator<E> inner_contents;
		
		private InnerIterator(Iterator<E> i) { inner_contents = i; }

		public boolean hasNext() 
		{
			return inner_contents.hasNext();
		}

		public E next() 
		{
			return inner_contents.next();
		}

		public void remove() 
		{
			assertNotFrozen();
			inner_contents.remove();
		}
	}
}
