package project;
import java.util.*;


public class BlackJack { 
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
