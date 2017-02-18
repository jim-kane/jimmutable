package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;

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
 * @param <T>
 */
abstract public class FieldSet<T> extends FieldCollection<T> implements Set<T>
{
	public FieldSet()
	{
		super();
		
	}
	
	public FieldSet(Iterable<T> objs)
	{
		super(objs);
	}
}
