package org.kane.base.examples.card;

import java.util.Objects;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.Validator;

import com.google.common.collect.ComparisonChain;

public class Card extends StandardImmutableObject<Card>
{
    private Suit suit; // Required
    private Value value; // Required
    
    
    // Builder Constructor
    private Card(Builder builder)
    {
        super();
    }
    
    // Default Constructor
    public Card(Suit suit, Value value)
    {
        super();
        
        this.suit = suit;
        this.value = value;
    } 
    
    
    public Suit getSimpleSuit() { return suit; }
    public Value getSimpleValue() { return value; }
    
    
    @Override
    public int compareTo(Card other)
    {
        return ComparisonChain.start()
                .compare(getSimpleSuit(), other.getSimpleSuit())
                .compare(getSimpleValue(), other.getSimpleValue())
                .result();
    }

    @Override
    public void freeze()
    {
        // Nothing to do
    }
    
    @Override
    public void normalize()
    {
        // Nothing to do
    }
    
    @Override
    public void validate()
    {
        Validator.notNull(suit);
        Validator.notEqual(suit, Suit.UNKNOWN);
        
        Validator.notNull(value);
        Validator.notEqual(value, Value.UNKNOWN);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(suit, value);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Card)) return false;
        
        Card other = (Card) obj;
        
        if (getSimpleSuit() != other.getSimpleSuit()) return false;
        if (getSimpleValue() != other.getSimpleValue()) return false;
        
        return true;
    }
    
    
    static public class Builder
    {
        private Card under_construction;
        
        public Builder()
        {
            under_construction = new Card(this);
        }
        
        public Builder(Card starting_point)
        {
            under_construction = starting_point.deepMutableCloneForBuilder();
        }
        
        // TODO assertNotComplete is annoying
        public void setSuit(Suit suit) 
        { 
            under_construction.assertNotComplete();
            under_construction.suit = suit;
        }
        
        public void setValue(Value value)
        {
            under_construction.assertNotComplete();
            under_construction.value = value;
        }
        
        /*
         * TODO
         * 1) Repeated calls to create() returns separate but equivalent objects
         * 2) create() can only be called once. Future calls to complete() or set* will throw Exception.
         */
        public Card create()
        {
            under_construction.complete();
            return under_construction;
        }
    }
}
