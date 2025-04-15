package project;
import java.util.*;


public class BlackJack { 
    public static void main(String[] args) {
        final int max_players = 4;
        Scanner scanner = new Scanner(System.in);
        Player[] players;
        Bot[] bots;
        Dealer dealer = new Dealer();

        //get a deck
        Deck deck = new Deck();
        deck.shuffleCards();

        //get rid of these
        boolean choosing = true;
        int x=0;

        //choose
        System.out.println("How many players");
        
        while (choosing){
            x = scanner.nextInt();
            if (x > max_players || x <= 0){
                System.out.println("Error: Between 1-4 Players");
            }
            else{
                System.out.println("Players:" + x + " Bots :" + (4-x));
                choosing = false;
            }
        }
        players = new Player[x];
            for (int i = 0; i < x; i++) {
                players[i] = new Player();
            }

            bots = new Bot[max_players - x];
            for (int i = 0; i < bots.length; i++) {
                bots[i] = new Bot();
            }
    //deal cards :)
    deck = dealCards(players,bots,dealer,deck);
    //players turns
    deck = playerTurns(players, deck, dealer);
    //bots turns
    deck = botTurn(deck, dealer, bots);
    //dealer turn
    deck = dealerTurn(deck, dealer);
    //Check who won
    checkWon(dealer, bots, players);
    }
    public static void checkWon(Dealer dealer, Bot[] bots, Player[] players){
        int dealerValue = dealer.getHandValue();
        System.out.println("Dealer had: " + dealerValue);
        int playerNo = 1;
        boolean dealerBust = dealerValue > 21;
        System.out.println("\n\n===Final Results===");

        for (Player player : players){
            int curPlayerValue = player.getHandValue();
            if (curPlayerValue > 21){ //player loses if greater than 21
                System.out.println("Player " + playerNo + " Busted");
            }
            else if(dealerBust){
                System.out.println("Player " + playerNo + " wins");
            } 
            else if(dealerValue < curPlayerValue){
                System.out.println("Player " + playerNo + " wins");
            }
            else if (dealerValue == curPlayerValue){
                System.out.println("Push for player " + playerNo);
            }
            else{
                System.out.println("Player " + playerNo + " Lost");
            }
            playerNo++;
        }
        for (Bot bot : bots){
            int curPlayerValue = bot.getHandValue();
            if (curPlayerValue > 21){ //player loses if greater than 21
                System.out.println("Bot " + playerNo + " Busted");
            }
            else if(dealerBust){
                System.out.println("Bot " + playerNo + " wins");
            } 
            else if(dealerValue < curPlayerValue){
                System.out.println("Bot " + playerNo + " wins");
            }
            else if (dealerValue == curPlayerValue){
                System.out.println("Push for bot " + playerNo);
            }
            else{
                System.out.println("Bot " + playerNo + " Lost");
            }
            playerNo++;
        }
        }
    
    public static Deck dealCards(Player[] players, Bot[] bots, Dealer dealer, Deck deck){
        System.out.println("Dealing...");
        //2 cards to each player
        for(Player player : players){
            player.getCard(deck.drawCard());
            player.getCard(deck.drawCard());
        }
        for(Bot bot : bots){
            bot.getCard(deck.drawCard());
            bot.getCard(deck.drawCard());
        }   
        dealer.getCard(deck.drawCard());
        //second card is hidden
        System.out.println("yay it worked");
        return deck;
        
    }
public static Deck playerTurns(Player[] players, Deck deck, Dealer dealer){
    Scanner scanner = new Scanner(System.in);
    int x = 0;
    for (Player player : players){
        x++;
        boolean standing = false;
        System.out.println("\n====Player " + x + "'s Turn=====" );
        while (!standing && player.getHandValue() <= 21){
            System.out.println("Dealer Visible Card:" + dealer.printHand() +
            "Your Hand:" + player.printHand());
            
            if (player.getHandValue() == 21){
                System.out.println("Blackjack!");
                break;
            }
            System.out.println("Hit or Stand (H/S)");
            String userInput = scanner.nextLine().toLowerCase().trim();
                while (!userInput.equals("h") && (!userInput.equals("s"))){
                    System.out.println("H for hit S for stand");
                    userInput = scanner.nextLine().toLowerCase().trim();
                }
            if (userInput.equals("h")){
                Card card = deck.drawCard();
                player.getCard(card);
                System.out.println("Drawn card: " + card.rank + " Suit: " + card.suit);
                int newHandValue = player.getHandValue();

                if (player.getHandValue() > 21){
                    System.out.println("Busted with:" + newHandValue);
                }
            }
            else{
                System.out.println("Stand with: " + player.getHandValue());
                standing = true;
            }
        }
    }
    return deck;
}
public static Deck botTurn(Deck deck, Dealer dealer, Bot[] bots){
    
    for(Bot bot: bots){
        int input = -1;
        while(input != 0){
            input = bot.hitOrStand(dealer);
            if (input == 1){
                bot.getCard(deck.drawCard());
            }

        }
    }
    return deck;
}
public static Deck dealerTurn(Deck deck, Dealer dealer){

    dealer.getCard(deck.drawCard());

    while (dealer.getHandValue() < 16){
    dealer.getCard(deck.drawCard());}
        
    return deck;
}


}
