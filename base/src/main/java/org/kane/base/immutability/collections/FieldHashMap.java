package org.kane.base.immutability.collections;

import java.util.HashMap;
import java.util.Map;

import org.kane.base.immutability.StandardImmutableObject;

public class FieldHashMap<K,V> extends FieldMap<K,V>
{
	public FieldHashMap()
	{
		super();
		
	}
	
	public FieldHashMap(StandardImmutableObject parent)
	{
		super(parent);
	}
	
	public FieldHashMap(StandardImmutableObject parent, Map<K,V> initial_values)
	{
		super(parent,initial_values);
	}
	
	protected Map<K, V> createNewMutableMapInstance() 
	{
		return new HashMap();
	}

}
