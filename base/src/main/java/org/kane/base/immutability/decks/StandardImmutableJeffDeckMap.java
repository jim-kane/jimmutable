package org.kane.base.immutability.decks;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldMap;


abstract public class StandardImmutableJeffDeckMap<T extends StandardImmutableJeffDeckMap<T, K, V>, K, V> extends StandardImmutableObject<T>
{
    abstract public FieldMap<K, V> getSimpleContents();
    
    
    @Override
    public int compareTo(T other)
    {
        return Integer.compare(getSimpleContents().size(), other.getSimpleContents().size());
    }

    @Override
    public void freeze()
    {
        getSimpleContents().freeze();
    }

    @Override
    public int hashCode()
    {
        return getSimpleContents().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! getClass().isInstance(obj)) return false;
        
        @SuppressWarnings("unchecked")
        StandardImmutableJeffDeckMap<T, K, V> other = (StandardImmutableJeffDeckMap<T, K, V>) obj;
        
        return getSimpleContents().equals(other.getSimpleContents());
    }
}
