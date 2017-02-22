package org.kane.base.examples.card;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableDeckList;


public class Deck extends StandardImmutableObject<Deck> implements StandardImmutableDeckList<Card>
{
    private FieldArrayList<Card> cards = new FieldArrayList<>();

    public Deck()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compareTo(Deck other)
    {
        return contentsCompareTo(other);
    }

    @Override
    public FieldList<Card> getSimpleContents()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void freeze()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void normalize()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void validate()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int hashCode()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
