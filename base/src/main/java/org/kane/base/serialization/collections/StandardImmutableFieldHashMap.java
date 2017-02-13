package org.kane.base.serialization.collections;

import java.util.HashMap;
import java.util.Map;

import org.kane.base.serialization.StandardImmutableObject;

public class StandardImmutableFieldHashMap<K,V> extends StandardImmutableFieldMap<K,V>
{
	public StandardImmutableFieldHashMap()
	{
		super();
		
	}
	
	public StandardImmutableFieldHashMap(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public StandardImmutableFieldHashMap(StandardImmutableObject parent, Map<K,V> initial_values)
	{
		super(parent,initial_values);
	}
	
	protected Map<K, V> createNewMutableMapInstance() 
	{
		return new HashMap();
	}

}
