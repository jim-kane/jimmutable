package org.kane.base.examples.card;

import java.util.Collection;
import java.util.Objects;

import org.kane.base.immutability.StandardImmutableJeffDeckList;
import org.kane.base.immutability.collections.FieldArrayList;
import org.kane.base.immutability.collections.FieldList;
import org.kane.base.serialization.Normalizer;
import org.kane.base.serialization.Validator;

import com.google.common.collect.ComparisonChain;


public class Hand extends StandardImmutableJeffDeckList<Hand, Card>
{
    private String name; // Optional, free form
    private int max_size; // Required, non-negative
    
    private FieldArrayList<Card> cards = new FieldArrayList<>();
    
    private Hand(Builder builder)
    {
    }
    
    public Hand(int max_size, Collection<Card> cards)
    {
        this(max_size, null, cards);
    }
    
    public Hand(int max_size, String name, Collection<Card> cards)
    {
        super();
        
        this.max_size = max_size;
        this.name = name;
        this.cards.addAll(cards);
        
        complete();
    }
    
    public int getSimpleID()
    {
        return max_size;
    }
    
    public String getOptionalName(String default_value)
    {
        if (null == name) return default_value;
        
        return name;
    }
    
    @Override
    public FieldList<Card> getSimpleContents()
    {
        return cards;
    }
    
    @Override
    public void normalize()
    {
        max_size = Normalizer.positive(max_size);
        
        name = Normalizer.trim(name);
    }

    @Override
    public void validate() 
    {
        Validator.min(max_size, 0);
        
        Validator.containsNoNulls(cards);
        Validator.containsOnlyInstancesOf(Card.class, cards);
        Validator.max(cards.size(), max_size);
    }
    
    @Override
    public int compareTo(Hand other)
    {
        int result = ComparisonChain.start()
                        .compare(max_size, other.max_size)
                        .compare(name, other.name)
                        .result();
        
        if (0 != result) return result;
        
        return super.compareTo(other);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(max_size, name, super.hashCode());
//        return Objects.hash(id, name, cards);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (! (obj instanceof Hand)) return false;
        
        Hand other = (Hand) obj;
        
        if (max_size != other.max_size) return false;
        if (! Objects.equals(name, other.name)) return false;
        
        return super.equals(obj);
    }


    static public class Builder
    {
        private Hand under_construction;
        
        public Builder()
        {
            under_construction = new Hand(this);
        }
        
        public Builder(Hand starting_point)
        {
            under_construction = starting_point.deepMutableCloneForBuilder();
        }
        
        public void setID(int id)
        {
            under_construction.max_size = id;
        }
        
        public void setName(String name)
        {
            under_construction.name = name;
        }

        public FieldList<Card> getCards()
        {
            return under_construction.getSimpleContents();
        }
        
        public Hand create()
        {
            return under_construction.deepClone();
        }
    }
}
