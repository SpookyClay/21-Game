package src;

public class Card{
    public String suit;
    public String rank;
    public int value;
    
    //Receives String of the rank String of the suit and int of the value
    // Stores the String of rank suit and the int of value for each card
    public Card(String suit, String rank, int value){
        this.suit = suit;
        this.rank = rank;
        this.value = value;

    }

}
