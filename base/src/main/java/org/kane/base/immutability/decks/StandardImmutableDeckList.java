package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldList;


/**
 * An interface (including some rather useful default methods) that all decks
 * which are backed by a List should implement.
 * 
 * The "model" for a list backed deck is org.kane.base.examples.BookDeckList.
 * One should pattern one's implementations on this class.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public interface StandardImmutableDeckList<T>
{
	public FieldList<T> getSimpleContents();
	
	
	/**
	 * Compare the contents to another deck
	 * 
	 * @param o
	 *            The other object
	 * @return
	 */
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckList) ) return 0;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	/**
	 * Test if the contents of this deck are equal to any other list backed deck
	 * 
	 * @param obj
	 *            The objec to compare to
	 * @return true if the contents are equal, false otherwise
	 */
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckList) ) return false;
		
		StandardImmutableDeckList other = (StandardImmutableDeckList)obj;
		
		return other.getSimpleContents().equals(getSimpleContents());
	}
	
	/**
	 * The default implementation of freeze (freeze the contents of the deck)
	 */
	default public void freezeContents()
	{
		getSimpleContents().freeze();
	}
	
	/**
	 * Get the hascode of the contents
	 * 
	 * @return The hashcode of the contents
	 */
	default public int hashContents()
	{
		return getSimpleContents().hashCode();
	}
}
