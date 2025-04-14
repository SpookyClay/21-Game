package project;
import java.util.*;

public class Deck{
    Random random  = new Random();
    Card[] cards;
    Card[] oldCards;

    Map<String, Integer> deck = new HashMap<>();

    
    public Deck(){
    cards = new Card[52*1]; //change 1 to change number of decks

    String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};

    deck.put("Two", 2);
    deck.put("Three", 3);
    deck.put("Four", 4);
    deck.put("Five", 5);
    deck.put("Six", 6);
    deck.put("Seven", 7);
    deck.put("Eight", 8);
    deck.put("Nine", 9);
    deck.put("Ten", 10);
    deck.put("Jack", 10);
    deck.put("Queen", 10);
    deck.put("King", 10);
    deck.put("Ace", 11);

    //make the deck
    int i =0;
    for (String suit : suits) {
        for (Map.Entry<String, Integer> cardNow : deck.entrySet()){
        cards[i] = new Card(suit, cardNow.getKey(), cardNow.getValue());
        i++;
        }
    }
    oldCards = cards;

    }

    public void shuffleCards(){
        for (int i = cards.length - 1; i > 0; i--){
            int j = random.nextInt(51);
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    public Card drawCard(){
        Card topCard = cards[cards.length-1];
        cards = Arrays.copyOf(cards, cards.length-1);
        return topCard;
    }

    public void newDeck(){
        cards = oldCards;
        shuffleCards();
    }
}
