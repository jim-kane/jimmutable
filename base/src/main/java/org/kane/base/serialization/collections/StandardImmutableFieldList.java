package org.kane.base.serialization.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.kane.base.serialization.ImmutableException;
import org.kane.base.serialization.StandardImmutableObject;
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
abstract public class StandardImmutableFieldList<T> implements List<T>
{
	/**
	 * This object will be null when the field is serialized from either XML and
	 * JSON (null = immutable) and will be "set" otherwise (via one of the
	 * constructors)
	 */
	transient private StandardImmutableObject parent;
	
	private List<T> contents;
	
	abstract protected List createNewMutableListInstance();
	abstract protected List createFieldSubList(StandardImmutableObject parent, Collection<T> objs);
	
	public StandardImmutableFieldList()
	{
		this(null);
		
	}
	
	public StandardImmutableFieldList(StandardImmutableObject parent)
	{
		this.parent = parent;
		contents = createNewMutableListInstance();
	}
	
	public StandardImmutableFieldList(StandardImmutableObject parent, Iterable<T> objs)
	{
		this(parent);
		
		if ( objs != null )
		{
			for ( T obj : objs )
			{
				add(obj);
			}
		}
	}


	
	public void assertNotComplete()
	{
		if ( parent == null ) // an unset parent can only occour when this object was created via xstream de-serialization... Therefore, the object is not mutable...
			throw new ImmutableException();
		else 
			parent.assertNotComplete();
	}
	
	
	public List<T> subList(int fromIndex, int toIndex)
	{
		return createFieldSubList(parent, contents.subList(fromIndex, toIndex));
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
		assertNotComplete();
		return contents.add(e);
	}
	
	public boolean remove(Object o) 
	{
		assertNotComplete();
		return contents.remove(o);
	}

	public boolean addAll(Collection<? extends T> c) 
	{
		assertNotComplete();
		return contents.addAll(c);
	}


	
	public boolean addAll(int index, Collection<? extends T> c) 
	{
		assertNotComplete();
		return contents.addAll(index,c);
	}


	
	public boolean removeAll(Collection<?> c) 
	{
		assertNotComplete();
		return contents.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) 
	{
		assertNotComplete();
		return contents.retainAll(c);
	}


	
	public void clear() 
	{
		assertNotComplete();
		contents.clear();
	}

	
	public T set(int index, T element) 
	{
		assertNotComplete();
		return contents.set(index,element);
	}


	
	public void add(int index, T element) 
	{
		assertNotComplete();
		contents.add(index,element);
	}

	public T remove(int index) 
	{
		assertNotComplete();
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
			assertNotComplete();
			itr.remove();
		}

		
		public void set(T e) 
		{
			assertNotComplete();
			itr.set(e);
		}

		
		public void add(T e) 
		{
			assertNotComplete();
			itr.add(e);
		}
	}
	
	
}
