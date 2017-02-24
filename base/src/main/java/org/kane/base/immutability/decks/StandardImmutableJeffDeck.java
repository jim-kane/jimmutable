package org.kane.base.immutability.decks;

import java.util.Iterator;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldCollection;


abstract public class StandardImmutableJeffDeck<T extends StandardImmutableJeffDeck<T, E>, E> extends StandardImmutableObject<T> implements Iterable<E>
{
    abstract public FieldCollection<E> getSimpleContents();
    
    
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
        StandardImmutableJeffDeck<T, E> other = (StandardImmutableJeffDeck<T, E>) obj;
        
        return getSimpleContents().equals(other.getSimpleContents());
    }
}
