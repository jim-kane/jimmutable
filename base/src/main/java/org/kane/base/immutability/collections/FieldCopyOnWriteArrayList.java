package org.kane.base.immutability.collections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * An implementation of a {@link CopyOnWriteArrayList} that begins life as mutable
 * but can, at any time, be "{@link #freeze() frozen}" (made immutable). In other
 * words, a wrapper for a {@link CopyOnWriteArrayList} that implements {@link Field}.
 * 
 * <p>Why does one need a thread safe immutable list? Well... if your
 * <em>construction</em> code (when the object is mutable) is threaded then it is
 * <em>much</em> safer to use a concurrent backing store. In Java, it is nigh
 * impossible to guarantee the mutable -> immutable transition in a multi-threaded
 * context absent a concurrent backing store.
 * 
 * <p>In general, however (when the field is being created in single threaded code),
 * just use {@link FieldArrayList}, which is <em>far</em> faster to construct.
 * 
 * @author Jim Kane
 *
 * @param <E> The type of elements in this list
 * 
 * @see FieldList
 * @see FieldArrayList
 */
@XStreamAlias("field-copy-on-write-array-list")
public class FieldCopyOnWriteArrayList<E> extends FieldList<E>
{
	/**
	 * Default constructor (for an empty list)
	 */
	public FieldCopyOnWriteArrayList()
	{
		super();
	}
	
	/**
     * Constructs a list containing the elements of the specified {@link Iterable},
     * in the order they are returned by the {@link Iterable#iterator() iterator}.
     *
     * @param objs The {code Iterable} whose elements are to be placed into this list
     * 
     * @throws NullPointerException if the specified {@code Iterable} is {@code null}
	 */
	public FieldCopyOnWriteArrayList(Iterable<E> objs)
	{
		super(objs);
	}
	
	@Override
	protected List<E> createNewMutableInstance() 
	{
		return new CopyOnWriteArrayList<>();
	}
}

