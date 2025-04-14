package project;

public class Bot extends AbstractPlayer{
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