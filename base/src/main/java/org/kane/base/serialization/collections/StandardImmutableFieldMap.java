package org.kane.base.serialization.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;

abstract public class StandardImmutableFieldMap<K,V> implements Map<K,V>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	transient private StandardImmutableObject parent;
	
	private Map<K,V> contents;
	
	abstract protected Map<K,V> createNewMutableMapInstance();
	
	public StandardImmutableFieldMap()
	{
		this(null);
		
	}
	
	public StandardImmutableFieldMap(StandardImmutableObject parent)
	{
		this.parent = parent;
		contents = createNewMutableMapInstance();
	}
	
	public StandardImmutableFieldMap(StandardImmutableObject parent, Map<K,V> initial_values)
	{
		this(parent);
		
		if ( initial_values != null )
			contents.putAll(initial_values);
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
	public boolean containsKey(Object key) { return contents.containsKey(key); }
	public boolean containsValue(Object value) { return contents.containsValue(value); }
	public V get(Object key) { return contents.get(key); }

	
	public V put(K key, V value)
	{
		assertNotComplete();
		return contents.put(key, value);
	}

	
	public V remove(Object key)
	{
		assertNotComplete();
		return contents.remove(key);
	}

	
	public void putAll(Map<? extends K, ? extends V> m)
	{
		assertNotComplete();
		contents.putAll(m);
	}

	
	public void clear()
	{
		assertNotComplete();
		contents.clear();
	}

	
	public Set<K> keySet()
	{
		return (Set<K>)new MyKeySet();
	}

	
	public Collection<V> values()
	{
		return new MyValuesCollection();
	}

	
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return new MyEntrySet();
	}

	private class MyKeySet<K> extends StandardImmutableFieldSet<K>
	{
		private MyKeySet()
		{
			super(parent);
		}
		
		protected Collection createNewMutableInstance()
		{
			return contents.keySet();
		}
	}
	
	private class MyEntrySet extends StandardImmutableFieldSet
	{
		private MyEntrySet()
		{
			super(parent);
		}
		
		protected Set createNewMutableInstance()
		{
			return contents.entrySet();
		}
	}
	
	private class MyValuesCollection<V> extends StandardImmutableFieldCollection<V>
	{
		private MyValuesCollection()
		{
			super(parent);
		}
		
		protected Collection<V> createNewMutableInstance()
		{
			return (Collection<V>)contents.values();
		}
	}

	
	public boolean equals(Object obj) 
	{
		if ( !(obj instanceof Map) ) return false;
		
		Map other = (Map)obj;
		
		if ( size() != other.size() ) return false;
		
		return entrySet().containsAll(other.entrySet());
	}

	
	public String toString() 
	{
		return contents.toString();
	}
	
	
}
