package org.kane.base.examples.card;


/**
 * The face value a standard deck of 52 playing cards
 * 
 * @author Jeff Dezso
 */
public enum Value
{
    ACE("ace", "A"),
    TWO("two", "2"),
    THREE("three", "3"),
    FOUR("four", "4"),
    FIVE("five", "5"),
    SIX("six", "6"),
    SEVEN("seven", "7"),
    EIGHT("eight", "8"),
    NINE("nine", "9"),
    TEN("ten", "10"),
    JACK("jack", "J"),
    QUEEN("queen", "Q"),
    KING("king", "K"),
    
    JOKER("joker", "Joker"),
    
    UNKNOWN("unknown", "Unknown Value");
    
    
    private String code; // required
    private String description; // required
    
    
    private Value(String code, String description)
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
    
    static public Value fromCode(String code, Value default_value)
    {
        if (code == null) return default_value;
        
        code = code.toLowerCase().trim();
        
        for (Value value : Value.values())
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
