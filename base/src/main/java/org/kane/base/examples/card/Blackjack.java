package org.kane.base.examples.card;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.kane.base.immutability.collections.FieldConcurrentHashMap;
import org.kane.base.immutability.collections.FieldMap;

/**
 * Mutable!
 * 
 * "Manager"
 * 
 * @author Jeff Dezso
 */
public class Blackjack
{
    static private final Blackjack INSTANCE = new Blackjack(); 
    
    static public Blackjack getInstance() { return INSTANCE; }
    
    static private final int MAX_HAND_VALUE = 21;
    static private final int MAX_HIT_VALUE = 16;
    
    static private final String DEALER = "Dealer";
    
    
    // Making this a Field is totally unnecessary, but interesting for testing
    final private FieldMap<String, Hand> players = new FieldConcurrentHashMap<>();
    
    private Deck deck;
    
    
    private Blackjack() {}
    
    public void addPlayer(String name)
    {
        if (players.containsKey(name)) throw new RuntimeException("Player with that name already exists!");
        
        players.put(name, new Hand(Integer.MAX_VALUE, name, Collections.emptyList()));
    }
    
    /**
     * @param name The player to remove
     * 
     * @return {@code true} if the game was modified by this method
     */
    public boolean removePlayer(String name)
    {
        return null != players.remove(name);
    }
    
    public Set<String> getSimplePlayers()
    {
        return Collections.unmodifiableSet(players.keySet());
    }
    
    public void play()
    {
        deck = Deck.createDefaultDeck(false, true);
        
        // Blackjack has to have a dealer
        addPlayer(DEALER);
        
        // Deal 2 cards to every player
        for (Map.Entry<String, Hand> player : players.entrySet())
        {
            player.setValue(deal(player.getValue(), 2));
        }
        
        printHands("STARTING HANDS");
        saveHands("init");
        loadHands("init");
        
        // Let each player player their hand
        for (Map.Entry<String, Hand> player : players.entrySet())
        {
            Hand hand = player.getValue();
            
            while (AI.shouldHit(hand))
            {
                hand = dealOneCard(hand);
            }
            
            // Record final states of hand
            player.setValue(hand);
        }
        
        printHands("ALL PLAYED");
        saveHands("played");
        loadHands("played");
        
        // Declare winner(s)
        Hand dealer_hand = players.get(DEALER);
        int dealer_value = AI.getValue(dealer_hand);
        
        for (Map.Entry<String, Hand> player : players.entrySet())
        {
            String name = player.getKey();
            Hand hand = player.getValue();
            
            if (DEALER.equals(name)) continue;
            
            if (AI.isBusted(dealer_hand))
            {
                System.out.println(name + " is a Winner!!!");
                continue;
            }
            
            if (AI.isBusted(hand)) continue;
            
            int player_value = AI.getValue(hand);
            
            if (player_value > dealer_value)
            {
                System.out.println(name + " is a Winner!!!");
            }
        }
    }
    
    private void saveHands(String suffix)
    {
        /* TODO: Reimplement
         * try
        {
            File dir = new File(System.getProperty("java.io.tmpdir") + "/blackjack");
            dir.mkdirs();
            
            {
                File dest = new File(dir, "deck-" + suffix + ".xml");
                Files.write(dest.toPath(), deck.toXML().getBytes());
            }

            for (Map.Entry<String, Hand> player : players.entrySet())
            {
                String name = player.getKey();
                Hand hand = player.getValue();

                File dest = new File(dir, name + "-" + suffix + ".xml");
                Files.write(dest.toPath(), hand.toXML().getBytes());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }
    
    private void loadHands(String suffix)
    {
       /* TODO: Reimplement
        * 
        *  try
        {
            File dir = new File(System.getProperty("java.io.tmpdir") + "/blackjack");
            dir.mkdirs();

            {
                File source = new File(dir, "deck-" + suffix + ".xml");
                Deck disk_deck = Hand.quietFromXML(new String(Files.readAllBytes(source.toPath())), null);
                
                if (! deck.equals(disk_deck))
                {
                    throw new RuntimeException("Decks not equal (" + source.getName() + ")");
                }
                
                this.deck = disk_deck;
            }
            
            for (Map.Entry<String, Hand> player : players.entrySet())
            {
                String name = player.getKey();
                Hand hand = player.getValue();

                File source = new File(dir, name + "-" + suffix + ".xml");
                Hand disk_hand = Hand.quietFromXML(new String(Files.readAllBytes(source.toPath())), null);
                
                if (! hand.equals(disk_hand))
                {
                    throw new RuntimeException("Hands not equal (" + source.getName() + ")");
                }
                
                player.setValue(disk_hand);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }
    
    private void printHands(String label)
    {
    	// TODO: Refactor to avoid Google Guava
       /* Joiner joiner = Joiner.on("\n\t").skipNulls();
        
        if (null != label)
        {
            System.out.println("***  " + label + "  ***");
        }
        
        for (Map.Entry<String, Hand> player : players.entrySet())
        {
            String name = player.getKey();
            Hand hand = player.getValue();
            
            System.out.println(name);
            System.out.println("\t" + joiner.join(hand));
            System.out.format("\tValue: %,d (%s)\n", AI.getValue(hand), AI.isBusted(hand) ? "Busted" : (AI.shouldHit(hand) ? "Hit" : "Stay"));
        }
        
        System.out.println();*/
    }
    
    synchronized private Hand dealOneCard(Hand hand)
    {
        Deck.Builder new_deck = new Deck.Builder(deck);
        Card card = new_deck.getCards().remove(0);
        deck = new_deck.create();
        
        Hand.Builder new_hand = new Hand.Builder(hand);
        new_hand.getCards().add(card);
        return new_hand.create();
    }
    
    private Hand deal(Hand hand, int num_cards)
    {
        for (int dealt = 0; dealt < num_cards; dealt++)
        {
            hand = dealOneCard(hand);
        }
        
        return hand;
    }
    
    static private class AI
    {
        static private int getValue(Card card, boolean treat_ace_high)
        {
            switch (card.getSimpleValue())
            {
                case ACE: return treat_ace_high ? 11 : 1;
                
                case TWO:   return 2;
                case THREE: return 3;
                case FOUR:  return 4;
                case FIVE:  return 5;
                case SIX:   return 6;
                case SEVEN: return 7;
                case EIGHT: return 8;
                case NINE:  return 9;
                case TEN:   return 10;
                
                case JACK:  return 10;
                case QUEEN: return 10;
                case KING:  return 10;
                
                default : return 0;
            }
        }
        
        static private int getValue(Hand hand, boolean treat_ace_high)
        {
            int value = 0;
            
            for (Card card : hand)
            {
                value += getValue(card, treat_ace_high);
            }
            
            return value;
        }
        
        static public int getValue(Hand hand)
        {
            int value = getValue(hand, true);
            
            if (value > MAX_HAND_VALUE)
            {
                value = getValue(hand, false);
            }
            
            return value;
        }
        
        static public boolean shouldHit(Hand hand)
        {
            return getValue(hand) <= MAX_HIT_VALUE;
        }
        
        static public boolean isBusted(Hand hand)
        {
            return getValue(hand) > MAX_HAND_VALUE;
        }
    }
    
    
    static public void main(String[] args)
    {
        getInstance().addPlayer("Jim");
        getInstance().addPlayer("Jeff");
        
        getInstance().play();
    }
}
