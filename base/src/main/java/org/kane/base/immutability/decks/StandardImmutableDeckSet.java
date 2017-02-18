package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldSet;

/**
 * An interface (including some rather useful default methods) that all decks
 * which are backed by a Set should implement.
 * 
 * The "model" for a list backed deck is org.kane.base.examples.BookDeckSet.
 * One should pattern one's implementations on this class.
 * 
 * @author jim.kane
 *
 * @param <T>
 */

public interface StandardImmutableDeckSet<T>
{
	public FieldSet<T> getSimpleContents();
	
	/**
	 * Compare the contents to another deck
	 * 
	 * @param o
	 *            The other object
	 * @return
	 */
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckSet) ) return 0;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	/**
	 * Test if the contents of this deck are equal to any other set backed deck
	 * 
	 * @param obj
	 *            The object to compare to
	 * @return true if the contents are equal, false otherwise
	 */
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckSet) ) return false;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)obj;
		
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
