package org.kane.base.immutability.decks;

import org.kane.base.immutability.collections.FieldMap;

public interface StandardImmutableDeckMap<K,V>
{
	public FieldMap<K,V> getSimpleContents();
	
	default public int getSimpleDeckContentsHashCode()
	{
		return getSimpleContents().hashCode();
	}
	
	default public int contentsCompareTo(Object o)
	{
		if ( !(o instanceof StandardImmutableDeckMap) ) return 0;
		
		StandardImmutableDeckMap other = (StandardImmutableDeckMap)o;
		return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
	}
	
	default public boolean contentsEqual(Object obj)
	{
		if ( !(obj instanceof StandardImmutableDeckMap) ) return false;
		
		StandardImmutableDeckMap other = (StandardImmutableDeckMap)obj;
		
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
