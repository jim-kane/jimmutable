package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * An implementation of a {@link List} that begins life as mutable but can, at
 * any time, be "{@link #freeze() frozen}" (made immutable). In other
 * words, a wrapper for a {@link List} that implements {@link Field}.
 * 
 * @author Jim Kane
 *
 * @param <E> The type of elements in this list
 * 
 * @see FieldCollection
 */
abstract public class FieldList<E> extends FieldCollection<E> implements List<E>
{
	/*
	 * Never access _contents_ directly.
	 * Use getContents so that SubList (and future) inheritance works
	 * correctly.
	 */
	private List<E> contents = createNewMutableInstance();
	
	@Override
	protected List<E> getContents() { return contents; }
	
	/**
	 * Instantiate a <em>new</em>, <em>mutable</em> {@link List}.
	 * This allows sub-classes to control the {@link List} implementation
	 * that is used (e.g. {@link ArrayList}, {@link LinkedList}, etc.).
	 *  
	 * @return The new {@link List} instance
	 */
	abstract protected List<E> createNewMutableInstance();
	
	
	/**
	 * Default constructor (for an empty list)
	 */
	public FieldList()
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
	public FieldList(Iterable<E> objs)
	{
		super(objs);
	}
	
	@Override
	public E get(int index)
	{
		return getContents().get(index);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) 
	{
		assertNotFrozen();
		return getContents().addAll(index,c);
	}
	
	@Override
	public E set(int index, E element) 
	{
		assertNotFrozen();
		return getContents().set(index,element);
	}
	
	@Override
	public void add(int index, E element) 
	{
		assertNotFrozen();
		getContents().add(index,element);
	}

	@Override
	public E remove(int index) 
	{
		assertNotFrozen();
		return getContents().remove(index);
	}

	@Override
	public int indexOf(Object o) { return getContents().indexOf(o); }
	
	@Override
	public int lastIndexOf(Object o) { return getContents().lastIndexOf(o); }

	@Override
	public ListIterator<E> listIterator() { return new MyListIterator(getContents().listIterator()); }
	
	@Override
	public ListIterator<E> listIterator(int index) { return new MyListIterator(getContents().listIterator(index)); }

	@Override
	public FieldList<E> subList(int from_index, int to_index)
	{
		return new SubList<>(this, getContents().subList(from_index, to_index));
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (! (obj instanceof List)) return false;
		
		// (Copied from AbstractList.equals)
		// Using ListIterator behaves will for both sequential and
		// random access lists (where other methods such as get may not)
        ListIterator<E> my_it = listIterator();
        ListIterator<?> other_it = ((List<?>) obj).listIterator();
        
		while (my_it.hasNext() && other_it.hasNext())
		{
			E my_elem = my_it.next();
			Object other_elem = other_it.next();
			
			if (! (my_elem == null ? other_elem == null : my_elem.equals(other_elem)))
			{
				return false;
			}
		}
		
		// Check that the sizes are the same
		return ! (my_it.hasNext() || other_it.hasNext());
	}

	/**
	 * An {@link ListIterator} that enforces {@link FieldCollection#freeze() freeze()}
	 * 
	 * @author Jim Kane
	 */
	private class MyListIterator implements ListIterator<E>
	{
		private ListIterator<E> itr;
		
		public MyListIterator(ListIterator<E> itr)
		{
			this.itr = itr;
		}
		
		public boolean hasNext() { return itr.hasNext(); }
		public E next() { return itr.next(); }

		public boolean hasPrevious() { return itr.hasPrevious(); }
		public E previous() { return itr.previous(); }
		public int nextIndex() { return itr.nextIndex(); }
		public int previousIndex() { return itr.previousIndex(); }

		public void remove() 
		{
			assertNotFrozen();
			itr.remove();
		}

		public void set(E e) 
		{
			assertNotFrozen();
			itr.set(e);
		}

		public void add(E e) 
		{
			assertNotFrozen();
			itr.add(e);
		}
	}
}

/**
 * A {@link FieldList} that enforces {@link FieldCollection#freeze() freeze()}
 * based on the parent
 * 
 * @author Jeff Dezso
 */
final class SubList<E> extends FieldList<E>
{
	private final FieldList<E> parent;
    private final List<E> contents;
	
	public SubList(FieldList<E> parent, List<E> contents)
	{
		if (null == parent) throw new NullPointerException(); // Any well-behaved List will not let this happen
		this.parent = parent;
        this.contents = contents;
	}
	
	/*
	 * Overriding these methods provides the indirection necessary
	 * for inheritance to do the heavy lifting while still connecting
	 * the key Field behavior of the parent and the sub-list
	 */
	
	@Override
	public List<E> getContents()
	{
		return contents;
	}
	
	@Override
	public void freeze()
	{
		parent.freeze();
	}

	@Override
	public boolean isFrozen()
	{
		return parent.isFrozen();
	}
	
	/*
	 * Dummied out but never used
	 */
	
	@Override
	protected List<E> createNewMutableInstance()
	{
		return parent.createNewMutableInstance();
	}
}
