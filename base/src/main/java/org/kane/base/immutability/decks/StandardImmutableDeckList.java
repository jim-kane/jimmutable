package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldList;

public interface StandardImmutableDeckList<T>
{
	public FieldList<T> getSimpleContents();
	
	default public int getSimpleDeckContentsHashCode()
	{
		return getSimpleContents().hashCode();
	}
	
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckList) ) return 0;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckList) ) return false;
		
		StandardImmutableDeckList other = (StandardImmutableDeckList)obj;
		
		return other.getSimpleContents().equals(getSimpleContents());
	}
	
	default public void freezeContents()
	{
		getSimpleContents().freeze();
	}
	
	default public int hashContents()
	{
		return getSimpleContents().hashCode();
	}
}
