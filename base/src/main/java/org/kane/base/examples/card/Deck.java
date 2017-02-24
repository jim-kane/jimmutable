package org.kane.base.examples.card;

import java.util.Collection;

import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.immutability.decks.StandardImmutableJeffDeckList;
import org.kane.base.serialization.Validator;


public class Deck extends StandardImmutableJeffDeckList<Deck, Card>
{
    private FieldArrayList<Card> cards = new FieldArrayList<>();
    
    private Deck(Builder builder)
    {
    }
    
    public Deck(Collection<Card> cards)
    {
        super();
        
        this.cards.addAll(cards);
        
        complete();
    }
    
    @Override
    public FieldList<Card> getSimpleContents()
    {
        return cards;
    }
    
    @Override
    public void normalize()
    {
    }

    @Override
    public void validate() 
    {
        Validator.containsNoNulls(cards);
        Validator.containsOnlyInstancesOf(Card.class, cards);
    }
    
    static public class Builder
    {
        private Deck under_construction;
        
        public Builder()
        {
            under_construction = new Deck(this);
        }
        
        public Builder(Deck starting_point)
        {
            under_construction = starting_point.deepMutableCloneForBuilder();
        }

        public FieldList<Card> getCards()
        {
            return under_construction.getSimpleContents();
        }
        
        public Deck create()
        {
            return under_construction.deepClone();
        }
    }
}
