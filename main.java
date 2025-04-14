import java.util.*;


public class main { 
    public static void main(String[] args) {
        boolean showdown = false;

        Scanner scanner = new Scanner(System.in);
        Deck deck = new Deck();
        deck.shuffleCards();
        Player player1 = new Player();
        Bot bot = new Bot();
        Dealer dealer = new Dealer();
        //player 1 gets cards then dealer

        player1.getCard(deck.drawCard());
        player1.getCard(deck.drawCard());
        bot.getCard(deck.drawCard());
        bot.getCard(deck.drawCard());
        dealer.getCard(deck.drawCard());


        while (player1.getHandValue() <= 21 && !showdown){
            System.out.println("Player 1: " + player1.printHand());
            System.out.println("Dealer: " + dealer.printHand() + "Hidden card");

            //check for 21
            if (player1.getHandValue() == 21){
                System.out.println("21!");
                showdown = true;
            }
            else{
            System.out.println("Hit/stand");
            String input = scanner.nextLine();
            if( ( input.equals("Hit"))){
                System.out.println(player1.getCard(deck.drawCard()));
            }
            else if (input.equals("Stand")){
                showdown = true;
            }
        }
        }
        showdown = false;
        while (bot.getHandValue() <= 21 && !showdown){
            System.out.println("Bot: " + bot.printHand());
            System.out.println("Dealer: " + dealer.printHand() + "Hidden card");

            //check for 21
            if (bot.getHandValue() == 21){
                System.out.println("21!");
                showdown = true;
            }
            else{
            System.out.println("Hit/stand");
            int input = bot.hitOrStand(dealer);
            if( ( input == 1)){
                System.out.println(bot.getCard(deck.drawCard()));
            }
            else if (input == 0){
                showdown = true;
            }
        }
        }
    // reveals cardup
    dealer.getCard(deck.drawCard()); 
    System.out.println("Player 1: " + player1.printHand());
    System.out.println("Dealer: " + dealer.printHand());
    //dealer draws\
    while (dealer.getHandValue() < 16){
        dealer.getCard(deck.drawCard());
        System.out.println("Player 1: " + player1.printHand());
    System.out.println("Dealer: " + dealer.printHand());
    }

    //win check
    if (player1.getHandValue() < dealer.getHandValue() || player1.getHandValue() > 21|| dealer.getHandValue() > 21 && player1.getHandValue() > 21){
        System.out.println("Dealer wins");
    }
    else if (player1.getHandValue() == dealer.getHandValue()){
        System.out.println("Push");
    }
    else{
        System.out.println("Player 1 wins");
    }

        
        





    scanner.close();
    }






}










abstract class abstractPlayer{
    protected ArrayList<Card> hand;
    protected int aceCount;


    public abstractPlayer(){
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

class Player extends abstractPlayer{
    public Player(){
        super();
    }
}

class Dealer extends abstractPlayer{
    public Dealer(){
        super();
    }

}

class Bot extends abstractPlayer{
    public Bot(){
        super();
    }

    public int getHardHandValue(){
        int hardValue = 0;
        for (Card card : hand){
            hardValue += card.value;
        }
        return hardValue;
    }
    public int hitOrStand(Dealer dealer){
        boolean isSoft = false;
        int dealerValue = dealer.getHandValue();
        int handValue = getHandValue();
        int hardHandValue = getHardHandValue();

        for (Card card : hand){
            if (card.rank.equals("Ace") && hardHandValue == handValue){
                isSoft = true;
            }
        }
        if (isSoft){
            return handleSoftHand(handValue, dealerValue);
        }
        else if(!isSoft){
            return handleHardHand(handValue, dealerValue);
        }
        return 0;}
    
    private int handleSoftHand(int handValue, int dealerValue){
        if (handValue >= 19){
            return 0;
        }
        else if (handValue == 18){
            return (dealerValue >= 9) ? 1 : 0; // hit on 9 10 ace
        }
        else{
            return 1;
        }

    }
    private int handleHardHand(int handValue, int dealerValue){
        if (handValue >= 17){
            return 0;
        }
        else if (handValue >= 13){
            return (dealerValue >= 7) ? 1 : 0;
        }
        else if (handValue == 12){
            return (dealerValue >= 4 && dealerValue <= 6) ? 0 : 1; // stand on 4 5 and 6
        }
        else{
            return 1;
        }
    }


}




class Card{
    public String suit;
    public String rank;
    public int value;
    

    public Card(String suit, String rank, int value){
        this.suit = suit;
        this.rank = rank;
        this.value = value;

    }

}


class Deck{
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