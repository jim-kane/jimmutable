package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.TreeSet;

import org.kane.base.immutability.StandardImmutableObject;

/**
 * An implementation of a Field object backed by an TreeSet. Generally
 * speaking, this Field will have all of the characteristics of an TreeSet
 * object that can be made immutable by calling freeze()
 * 
 * Note: TreeSet, and consequently FieldTreeSet is not thread safe. This is
 * *generally* not a concern once "frozen" but if you a construction process
 * that is multi-threaded you should use FieldConcurrentHashSet 
 * 
 * In the "standard" case (construction in one thread) don't worry about any of
 * this and just use FieldHashSet or FieldTreeSet.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public class FieldTreeSet<T> extends FieldSet<T>
{
	public FieldTreeSet()
	{
		super();
	}
	
	public FieldTreeSet(Iterable<T> objs)
	{
		super(objs);
	}
	
	protected Collection<T> createNewMutableInstance()
	{
		return new TreeSet();
	}

}
