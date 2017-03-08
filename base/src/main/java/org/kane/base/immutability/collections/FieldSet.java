package org.kane.base.immutability.collections;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of a {@link Set} that begins life as mutable but can, at
 * any time, be "{@link #freeze() frozen}" (made immutable). In other
 * words, a wrapper for a {@link Set} that implements {@link Field}.
 * 
 * @author Jim Kane
 *
 * @param <E> The type of elements in this set
 * 
 * @see FieldCollection
 */
abstract public class FieldSet<E> extends FieldCollection<E> implements Set<E>
{
	/*
	 * Never access _contents_ directly.
	 * Use getContents so that inheritance works correctly.
	 */
	private Set<E> contents = createNewMutableInstance();
	
	@Override
	protected Set<E> getContents() { return contents; }
	
	/**
	 * Instantiate a <em>new</em>, <em>mutable</em> {@link Set}.
	 * This allows sub-classes to control the {@link Set} implementation
	 * that is used (e.g. {@link HashSet}, {@link TreeSet}, etc.).
	 *  
	 * @return The new {@link Set} instance
	 */
	abstract protected Set<E> createNewMutableInstance();
	
	
	/**
	 * Default constructor (for an empty set)
	 */
	public FieldSet()
	{
		super();
	}
	
	/**
     * Constructs a set containing the elements of the specified {@link Iterable}
     *
     * @param objs The {code Iterable} whose elements are to be placed into this set
     * 
     * @throws NullPointerException if the specified {@code Iterable} is {@code null}
	 */
	public FieldSet(Iterable<E> objs)
	{
		super(objs);
	}
}
