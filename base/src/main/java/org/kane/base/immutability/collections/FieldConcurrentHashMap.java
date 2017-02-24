package org.kane.base.immutability.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of a Field object backed by an ConcurrentHashMap. Generally
 * speaking, this Field will have all of the characteristics of a
 * ConcurrentHashMap object that can be made immutable by calling freeze()
 * 
 * Why does one need a thread safe immutable map? Well... if your *construction*
 * code (when the object is mutable) is threaded then it is *much safer* to use
 * this map as, the java language being what it is, it is neigh on impossible to
 * guarantee the mutable -> immutable transition in this multithreaded context
 * absent a concurrent backing store.
 * 
 * In general, however (when the field is being created in single threaded code)
 * don't worry about a thing and just use FieldHashMap.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public class FieldConcurrentHashMap <K,V> extends FieldMap<K,V>
{
	public FieldConcurrentHashMap()
	{
		super();
	}
	
	public FieldConcurrentHashMap(Map<K,V> initial_values)
	{
		super(initial_values);
	}
	
	protected Map<K, V> createNewMutableInstance() 
	{
		return new ConcurrentHashMap<>();
	}
}
