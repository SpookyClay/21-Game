package src;
import java.util.*;

public class Deck{
    Random random  = new Random();
    Card[] cards;
    Card[] oldCards;

    Map<String, Integer> deck = new HashMap<>();

    //constructor
    // This constructor makes the deck for the game
    public Deck(){
    cards = new Card[52*1]; //change 1 to change number of decks

    String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};
    //assigns string to its card value
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

    //makes the deck in hashmap order
    int i =0;
    for (String suit : suits) {
        for (Map.Entry<String, Integer> cardNow : deck.entrySet()){
        cards[i] = new Card(suit, cardNow.getKey(), cardNow.getValue());
        i++;
        }
    }
    oldCards = cards; //this is so that the shuffled cards remain for the next game

    }
/*Takes in no variables
* This method shuffles the deck
* No return as deck is held in this class */
    public void shuffleCards(){
        for (int i = cards.length - 1; i > 0; i--){//For each card in the deck
            int j = random.nextInt(51);// Generates a random number in the array
            Card temp = cards[i];
            cards[i] = cards[j];//Swaps the two cards's spots
            cards[j] = temp;
        }
    }
    /*Takes in no variables
    Returns the card (Card) at the top of the array*/
    public Card drawCard(){
        Card topCard = cards[cards.length-1];
        cards = Arrays.copyOf(cards, cards.length-1);
        return topCard;
    }
/* Takes in no variables
* When the user wants to make a new game it takes the old deck, shuffles it and that is the new deck
* Returns nothing because the deck is held in this class*/
    public void newDeck(){
        cards = oldCards;
        shuffleCards();
    }
}
