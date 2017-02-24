package org.kane.base.immutability;

import java.util.Iterator;

import org.kane.base.immutability.collections.FieldList;

abstract public class StandardImmutableJeffDeckList<T extends StandardImmutableJeffDeckList<T, E>, E extends StandardImmutableObject<E>> extends StandardImmutableObject<T> implements Iterable<E>
{
    abstract public FieldList<E> getSimpleContents();
    
    
    @Override
    public Iterator<E> iterator()
    {
        return getSimpleContents().iterator();
    }
    
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
        StandardImmutableJeffDeckList<T, E> other = (StandardImmutableJeffDeckList<T, E>) obj;
        
        return getSimpleContents().equals(other.getSimpleContents());
    }
}
