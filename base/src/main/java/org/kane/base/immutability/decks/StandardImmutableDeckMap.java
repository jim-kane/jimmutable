package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldMap;

/**
 * An interface (including some rather useful default methods) that all decks
 * which are backed by a Map should implement.
 * 
 * The "model" for a list backed deck is org.kane.base.examples.BookDeckMap.
 * One should pattern one's implementations on this class.
 * 
 * @author jim.kane
 *
 * @param <T>
 */
public interface StandardImmutableDeckMap<K,V>
{
	public FieldMap<K,V> getSimpleContents();
	
	/**
	 * Compare the contents to another deck
	 * 
	 * @param o
	 *            The other object
	 * @return
	 */
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckMap) ) return 0;
		
		StandardImmutableDeckMap other = (StandardImmutableDeckMap)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	/**
	 * Test if the contents of this deck are equal to any other map backed deck
	 * 
	 * @param obj
	 *            The object to compare to
	 * @return true if the contents are equal, false otherwise
	 */
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckMap) ) return false;
		
		StandardImmutableDeckMap other = (StandardImmutableDeckMap)obj;
		
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
