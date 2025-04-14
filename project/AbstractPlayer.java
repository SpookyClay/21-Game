package project;
import java.util.ArrayList;
public abstract class AbstractPlayer{
    protected ArrayList<Card> hand;
    protected int aceCount;


    public AbstractPlayer(){
        this.hand = new ArrayList<Card>();
        this.aceCount = 0;


    }
    public int getCard(Card card){
                hand.add(card);
                return getHandValue();
            }
    
    public int getHandValue(){
        int value = 0;
        for (Card card : hand){
            value += card.value;
            if (card.rank.equals("Ace")){
            aceCount++;
            }
        }
        while (value > 21 && aceCount > 0){
            value -= 10;
            aceCount--;
        }
        return value;
        
    }
    public String printHand(){
        String output = "";
        for (Card card : hand){
            output += card.rank + " of " + card.suit + "\n";
        }
        return output;
    }


}