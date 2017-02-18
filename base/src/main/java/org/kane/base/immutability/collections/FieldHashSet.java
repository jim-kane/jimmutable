package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.HashSet;

import org.kane.base.immutability.StandardImmutableObject;

/**
 * An implementation of a Field object backed by an HashSet. Generally
 * speaking, this Field will have all of the characteristics of an HashSet
 * object that can be made immutable by calling freeze()
 * 
 * Note: HashSet, and consequently FieldHashSet is not thread safe. This is
 * *generally* not a concern once "frozen" but if you a construction process
 * that is multi-threaded you should use FieldConcurrentHashSet 
 * 
 * In the "standard" case (construction in one thread) don't worry about any of
 * this and just use FieldHashSet.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public class FieldHashSet<T> extends FieldSet<T>
{
	public FieldHashSet()
	{
		super();
	}
	
	public FieldHashSet(Iterable<T> objs)
	{
		super(objs);
	}
	
	protected Collection<T> createNewMutableInstance()
	{
		return new HashSet();
	}

}
