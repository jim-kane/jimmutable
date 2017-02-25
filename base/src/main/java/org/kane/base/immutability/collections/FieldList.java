package org.kane.base.immutability.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * This is a utility class that makes it easy to create a properly behaved list
 * field of an StandardImmutableObject.
 * 
 * What's hard about that? If you have to ask... ;)
 * 
 * Java has a number of really quirky mutability issues... For example, think
 * about the following code:
 * 
 *  List<String> my_list = new ArrayList();
 *  Iterator<String> itr = my_list.iterator();
 *  
 *  my_list.add("foo");
 *  my_list.add("bar");
 *  
 *  my_list = Collections.unmodifiableList(my_list);
 *  
 *  itr.remove();
 *  
 *  Believe it or not, this code *REMOVES FOO*...
 *  
 *  So, immutable coder beware... and use FieldList (or one of its implementing classes)!  
 *  (which will correctly throw an ImmutableException in this case)
 * 
 * @author jim.kane
 *
 * @param <E>
 */
abstract public class FieldList<E> extends FieldCollection<E> implements List<E>
{
	private List<E> contents = createNewMutableInstance();
	
	@Override
	protected List<E> getContents() { return contents; }
	
	abstract protected List<E> createNewMutableInstance();
	
	public FieldList()
	{
		super();
	}
	
	public FieldList(Iterable<E> objs)
	{
		this();
		
		if ( objs == null ) objs = Collections.emptyList();
		
		for ( E obj : objs )
		{
			add(obj);
		}
	}

	public List<E> subList(int fromIndex, int toIndex)
	{
		return Collections.unmodifiableList(contents.subList(fromIndex, toIndex));
	}
	
	public E get(int index) { return contents.get(index); }
	
	public boolean addAll(int index, Collection<? extends E> c) 
	{
		assertNotFrozen();
		return contents.addAll(index,c);
	}
	
	public E set(int index, E element) 
	{
		assertNotFrozen();
		return contents.set(index,element);
	}
	
	public void add(int index, E element) 
	{
		assertNotFrozen();
		contents.add(index,element);
	}

	public E remove(int index) 
	{
		assertNotFrozen();
		return contents.remove(index);
	}

	public int indexOf(Object o) { return contents.indexOf(o); }
	public int lastIndexOf(Object o) { return contents.lastIndexOf(o); }

	public ListIterator<E> listIterator() { return new MyListIterator(contents.listIterator()); }
	public ListIterator<E> listIterator(int index) { return new MyListIterator(contents.listIterator(index)); }

	public boolean equals(Object obj) 
	{
		if (!(obj instanceof List) ) return false;
		
		@SuppressWarnings("unchecked")
		List<E> other = (List<E>)obj;
		
		if ( other.size() != size() ) return false;
		
		for ( int i = 0; i < size(); i++ )
		{
			if ( !get(i).equals(other.get(i)) ) 
				return false;
		}
		
		return true;
	}

	private class MyListIterator implements ListIterator<E>
	{
		private ListIterator<E> itr;
		
		public MyListIterator(ListIterator<E> itr)
		{
			this.itr = itr;
		}
		
		public boolean hasNext() { return itr.hasNext(); }
		public E next() { return (E)itr.next(); }

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
