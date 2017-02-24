package org.kane.base.immutability.collections;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * An implementation of a Field (Set) object backed by an ConcurrentHashMap (via
 * Collections.newSetFromMap(new ConcurrentHashMap())). Generally speaking, this
 * Field will have all of the characteristics of a Concurrent HashSet object
 * that can be made immutable by calling freeze()
 * 
 * Why does one need a thread safe immutable set? Well... if your *construction*
 * code (when the object is mutable) is threaded then it is *much safer* to use
 * this set as, the java language being what it is, it is neigh on impossible to
 * guarantee the mutable -> immutable transition in this multithreaded context
 * absent a concurrent backing store.
 * 
 * In general, however (when the field is being created in single threaded code)
 * don't worry about a thing and just use FieldHashSet.
 * 
 * @author jim.kane
 *
 * @param <T>
 */

public class FieldConcurrentHashSet<T> extends FieldSet<T>
{
	public FieldConcurrentHashSet()
	{
		super();
	}
	
	public FieldConcurrentHashSet(Iterable<T> objs)
	{
		super(objs);
	}
	
	protected Set<T> createNewMutableInstance()
	{
		return Collections.newSetFromMap(new ConcurrentHashMap<>());
	}
}