package project;

public class Bot extends AbstractPlayer{
    //Constructor
    public Bot(){
        super();
    }
    //This method calculates the hard value of the hand meaning aces are woth 11
    //Returns this hard value
    public int getHardHandValue(){
        int hardValue = 0;
        for (Card card : hand){
            hardValue += card.value;
        }
        return hardValue;
    }
    //This method is the logic for deciding whether a bot will hit or not
    //It requires the dealer upcard value as that is the main thing in blackjack basic strategy
    //Returns a 1 for hit and 0 for stand
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

    /* The following two methods are for playing basic strategy blackjack
    * Both need the hand value and the dealer value
    * Depending on the dealer value they will act differently on different hand values
    * Both return 1 when hit, 0 when stand*/


    private int handleSoftHand(int handValue, int dealerValue){
        if (handValue >= 19){ //Above 19 stand
            return 0;
        }
        else if (handValue == 18){ //When hand is 18
            return (dealerValue >= 9) ? 1 : 0; // hit on 9 10 j,q,k, ace
        }
        else{ //otherwise hit
            return 1;
        }

    }
    private int handleHardHand(int handValue, int dealerValue){
        if (handValue >= 17){ //Greater or equal to 17 stand
            return 0;
        }
        else if (handValue >= 13){ //13 and above
            return (dealerValue >= 7) ? 1 : 0;// hit if dealer hand is 7 and hiher
        }
        else if (handValue == 12){
            return (dealerValue >= 4 && dealerValue <= 6) ? 0 : 1; // stand on 4 5 and 6
        }
        else{ //otherwise hit
            return 1;
        }
    }


}