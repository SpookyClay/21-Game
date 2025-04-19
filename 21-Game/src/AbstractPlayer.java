package src;
import java.util.ArrayList;
public abstract class AbstractPlayer{
    protected ArrayList<Card> hand;
    protected int aceCount;

 //Constructor
    public AbstractPlayer(){
        this.hand = new ArrayList<Card>();
        this.aceCount = 0;


    }
    /*When a player hits or when recieveing cards this method is called
    * returns the hand value*/
    public int getCard(Card card){
                hand.add(card);
                return getHandValue();
            }
    /* This method gets the hand value of a hand
    * returns an int of the hand value*/
    public int getHandValue(){
        int value = 0;
        for (Card card : hand){
            value += card.value;
            if (card.rank.equals("Ace")){ //this counts the amount of aces
            aceCount++;
            }
        }
        while (value > 21 && aceCount > 0){ //if there is an ace it will decrease by 10 (making ace worth 1)
            value -= 10;
            aceCount--;
        }
        return value;
        
    }

    /*This method builds the card :)
    * Card is built using StringBuilder line by line
    * Returns cards as string*/
    public String printHand() {
        StringBuilder[] cardLines = new StringBuilder[7];

        for (int i = 0; i < 7; i++) {
            cardLines[i] = new StringBuilder();
        }

        for (Card card : hand) { //Builds the card line by line
            String rank = card.rank.substring(0, 1);
            String suitSymbol = getSuitSymbol(card.suit);
            String colorCode = (suitSymbol.equals("♥") || suitSymbol.equals("♦")) ? "\u001B[31m" : "\u001B[30m";
            cardLines[0].append(colorCode).append("┌─────────┐").append("\u001B[0m").append(" ");
            cardLines[1].append(colorCode).append(String.format("│%-9s│", rankToCharacter(card.rank))).append("\u001B[0m "); // 9 spaces minus means from the left
            cardLines[2].append(colorCode).append("│         │").append("\u001B[0m ");
            cardLines[3].append(colorCode).append(String.format("│    %-2s   │", suitSymbol)).append("\u001B[0m ");
            cardLines[4].append(colorCode).append("│         │").append("\u001B[0m ");
            cardLines[5].append(colorCode).append(String.format("│%9s│", rankToCharacter(card.rank))).append("\u001B[0m "); // 9 spaces
            cardLines[6].append(colorCode).append("└─────────┘").append("\u001B[0m ");
        }
        StringBuilder result = new StringBuilder();
        for (StringBuilder line : cardLines) {
            result.append(line.toString()).append("\n");
        }
        return result.toString();
    }


    /*Receives card suit (String)
     * Figures out what symbol to print on the card
     * Returns that symbol as a string*/
    private String getSuitSymbol(String suit) {
        return switch (suit.toLowerCase()) {
            case "hearts" -> "♥";
            case "diamonds" -> "♦";
            case "clubs" -> "♣";
            case "spades" -> "♠";
            default -> "oops";
        };
    }
    /*Receives card rank (String)
    * Figures out what character to print on the card
    * Returns that character as a string*/
    private static String rankToCharacter(String rank) {
        return switch (rank.toLowerCase()) {
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            case "ten" -> "10";
            case "jack" -> "J";
            case "queen" -> "Q";
            case "king" -> "K";
            case "ace" -> "A";
            default -> "er";
        };
    }
}