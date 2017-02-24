package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of a Field object backed by an HashMap. Generally speaking,
 * this Field will have all of the characteristics of a HahsMap object that can
 * be made immutable by calling freeze()
 * 
 * Please note: HashMap is *not* threadsafe while mutable. Once frozen the
 * object is, for the most part, thread safe. That being said, if your
 * construction process is threaded, you should make use of
 * FieldConcurrentHashMap as the thread safety of the transition from mutable to
 * immutable is a complicated and hard to guarantee one.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public class FieldHashMap<K,V> extends FieldMap<K,V>
{
	public FieldHashMap()
	{
		super();
	}
	
	public FieldHashMap(Map<K,V> initial_values)
	{
		super(initial_values);
	}
	
	protected Map<K, V> createNewMutableInstance() 
	{
		return new HashMap<>();
	}
}
