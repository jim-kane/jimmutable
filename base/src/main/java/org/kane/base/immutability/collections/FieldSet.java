package org.kane.base.immutability.collections;

import java.util.Set;

/**
 * An implementation of a Set that begins life as immutable but can, at
 * any time, be "frozen" (made immutable) by calling the freeze method. In other
 * words, a Set implementation that also implements Field.
 * 
 * Do not fear extending this collection object as needed, such implementations
 * tend to go very quickly as the base class does nearly all of the work for
 * you. Notwithstanding the foregoing, would be extenders of this class should
 * take time to carefully understand the immutability principles involved and
 * write careful unit tests to make sure that their implementations are as
 * strictly immutable as possible.
 * 
 * 
 * @author jim.kane
 *
 * @param <E>
 */
abstract public class FieldSet<E> extends FieldCollection<E> implements Set<E>
{
	private Set<E> contents = createNewMutableInstance();
	
	@Override
	protected Set<E> getContents() { return contents; }
	
	abstract protected Set<E> createNewMutableInstance();
	
	public FieldSet()
	{
		super();
	}
	
	public FieldSet(Iterable<E> objs)
	{
		super(objs);
	}
}
