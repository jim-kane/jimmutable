package org.kane.base.examples.card;

import java.util.Objects;

import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.Validator;

import com.google.common.collect.ComparisonChain;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("card")
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
    
    @Override
    public String toString()
    {
        if (Suit.JOKER == suit) return "Joker";
        
        return value + " of " + suit;
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
        
        public void setSuit(Suit suit) 
        {
            under_construction.suit = suit;
        }
        
        public void setValue(Value value)
        {
            under_construction.value = value;
        }
        
        public Card create()
        {
            return under_construction.deepClone();
        }
    }
}
