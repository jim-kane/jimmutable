package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldSet;

public interface StandardImmutableDeckSet<T>
{
	public FieldSet<T> getSimpleContents();
	
	default public int getSimpleDeckContentsHashCode()
	{
		return getSimpleContents().hashCode();
	}
	
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckSet) ) return 0;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckSet) ) return false;
		
		StandardImmutableDeckSet other = (StandardImmutableDeckSet)obj;
		
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
