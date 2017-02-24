package org.kane.base.examples.card;


/**
 * The suit for a standard deck of 52 playing cards
 * 
 * @author Jeff Dezso
 */
public enum Suit
{
    SPADES("spades", "Spades"),
    HEARTS("hearts", "Hearts"),
    CLUBS("clubs", "Clubs"),
    DIAMONDS("diamonds", "Diamonds"),
    
    JOKER("joker", "Joker"),
    
    UNKNOWN("unknown", "Unknown Suit Type");
    
    
    private String code; // required
    private String description; // required
    
    
    private Suit(String code, String description)
    {
        this.code = code.toLowerCase().trim();
        this.description = description.trim();
    }
    
    
    /*** Getters ***/
    
    public String getSimpleCode()
    {
        return code;
    }
    
    public String getSimpleDescription()
    {
        return description;
    }
    
    
    /*** Helper Methods ***/
    
    static public Suit fromCode(String code, Suit default_value)
    {
        if (code == null) return default_value;
        
        code = code.toLowerCase().trim();
        
        for (Suit value : Suit.values())
        {
            if (value.getSimpleCode().equals(code))
            {
                return value;
            }
        }
        
        return default_value;
    }
    
    @Override
    public String toString()
    {
        return getSimpleCode();
    }
}
