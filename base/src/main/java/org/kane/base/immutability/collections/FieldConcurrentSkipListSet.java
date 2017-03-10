package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.kane.base.immutability.collections.FieldCollection.FieldCollectionConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;


/**
 * An implementation of a {@link ConcurrentSkipListSet} that begins life as mutable
 * but can, at any time, be "{@link #freeze() frozen}" (made immutable). In other
 * words, a wrapper for a {@link ConcurrentSkipListSet} that implements {@link Field}.
 * 
 * <p>Why does one need a thread safe immutable list? Well... if your
 * <em>construction</em> code (when the object is mutable) is threaded then it is
 * <em>much</em> safer to use a concurrent backing store. In Java, it is nigh
 * impossible to guarantee the mutable -> immutable transition in a multi-threaded
 * context absent a concurrent backing store.
 * 
 * <p>In general, however (when the field is being created in single threaded code),
 * just use {@link FieldTreeSet}, which is <em>far</em> faster to construct.
 * 
 * @author Jim Kane
 *
 * @param <E> The type of elements in this list
 * 
 * @see FieldList
 * @see FieldTreeSet
 */
@XStreamAlias("field-concurrent-skip-list-set")
@XStreamConverter(FieldConcurrentSkipListSet.MyConverter.class)
final public class FieldConcurrentSkipListSet<E> extends FieldSet<E>
{
	/**
	 * Default constructor (for an empty set)
	 */
	public FieldConcurrentSkipListSet()
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
	public FieldConcurrentSkipListSet(Iterable<E> objs)
	{
		super(objs);
	}

	@Override
	protected Set<E> createNewMutableInstance()
	{
		return new ConcurrentSkipListSet<>();
	}
	
	static public class MyConverter extends FieldCollectionConverter
	{
		Class getFieldClass() { return FieldConcurrentSkipListSet.class; }
		Collection createNewMutableInstance() { return new FieldConcurrentSkipListSet(); }
	}
}
