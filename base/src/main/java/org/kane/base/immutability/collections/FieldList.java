package org.kane.base.immutability.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.kane.base.immutability.ImmutableException;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.ValidationException;

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
 *  So, immutable coder beware... and use AbstractStandardImmutableFieldList!  
 *  (which will correctly throw an ImmutableException in this case)
 * 
 * @author jim.kane
 *
 * @param <T>
 */
abstract public class FieldList<T> implements List<T>, Field
{
	transient private boolean is_frozen = true;
	
	private List<T> contents;
	
	abstract protected List createNewMutableListInstance();
	
	public FieldList()
	{
		is_frozen = false;
		contents = createNewMutableListInstance();
	}
	
	public FieldList(Iterable<T> objs)
	{
		this();
		
		if ( objs == null ) objs = Collections.EMPTY_LIST;
		
		for ( T obj : objs )
		{
			add(obj);
		}
	}

	public void freeze() { is_frozen = true; }
	public boolean getSimpleIsFrozen()  { return is_frozen; }
	
	
	public List<T> subList(int fromIndex, int toIndex)
	{
		return Collections.unmodifiableList(contents.subList(fromIndex, toIndex));
	}
	
	public int size() { return contents.size(); }
	public boolean isEmpty() { return contents.isEmpty(); }
	public boolean contains(Object o) { return contents.contains(o); }
	public Object[] toArray() { return contents.toArray();	}
	public <T> T[] toArray(T[] a) { return contents.toArray(a); }
	public boolean containsAll(Collection<?> c) { return contents.containsAll(c); }
	public T get(int index) { return contents.get(index); }
	
	

	public boolean add(T e) 
	{
		assertNotFrozen();
		return contents.add(e);
	}
	
	public boolean remove(Object o) 
	{
		assertNotFrozen();
		return contents.remove(o);
	}

	public boolean addAll(Collection<? extends T> c) 
	{
		assertNotFrozen();
		return contents.addAll(c);
	}


	
	public boolean addAll(int index, Collection<? extends T> c) 
	{
		assertNotFrozen();
		return contents.addAll(index,c);
	}


	
	public boolean removeAll(Collection<?> c) 
	{
		assertNotFrozen();
		return contents.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) 
	{
		assertNotFrozen();
		return contents.retainAll(c);
	}


	
	public void clear() 
	{
		assertNotFrozen();
		contents.clear();
	}

	
	public T set(int index, T element) 
	{
		assertNotFrozen();
		return contents.set(index,element);
	}


	
	public void add(int index, T element) 
	{
		assertNotFrozen();
		contents.add(index,element);
	}

	public T remove(int index) 
	{
		assertNotFrozen();
		return contents.remove(index);
	}

	public int indexOf(Object o) { return contents.indexOf(o); }
	public int lastIndexOf(Object o) { return contents.lastIndexOf(o); }


	public Iterator<T> iterator() { return new MyListIterator(contents.listIterator()); }
	public ListIterator<T> listIterator() { return new MyListIterator(contents.listIterator()); }
	public ListIterator<T> listIterator(int index) { return new MyListIterator(contents.listIterator(index)); }
	

	public int hashCode() 
	{
		return contents.hashCode();
	}


	public boolean equals(Object obj) 
	{
		if (!(obj instanceof List) ) return false;
		
		List other = (List)obj;
		
		if ( other.size() != size() ) return false;
		
		for ( int i = 0; i < size(); i++ )
		{
			if ( !get(i).equals(other.get(i)) ) 
				return false;
		}
		
		return true;
	}

	public String toString() 
	{
		return super.toString();
	}

	private class MyListIterator implements ListIterator<T>
	{
		private ListIterator<T> itr;
		
		public MyListIterator(ListIterator<T> itr)
		{
			this.itr = itr;
		}

		
		public boolean hasNext() { return itr.hasNext(); }
		public T next() { return (T)itr.next(); }

		
		public boolean hasPrevious() { return itr.hasPrevious(); }
		public T previous() { return itr.previous(); }
		public int nextIndex() { return itr.nextIndex(); }
		public int previousIndex() { return itr.previousIndex(); }

		
		public void remove() 
		{
			assertNotFrozen();
			itr.remove();
		}

		
		public void set(T e) 
		{
			assertNotFrozen();
			itr.set(e);
		}

		
		public void add(T e) 
		{
			assertNotFrozen();
			itr.add(e);
		}
	}
	
	
}
