package src;
import java.util.*;

public class BlackJackConsole {
    //Color for text
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";

    public static int[] roundsWon;
    public static int[] roundsLost;
    public static int[] roundsTied;
    //Main
    public static void main(String[] args) {

        //Get number of players
        int numPlayers = getNumberPlayers();
        int numBots;
        //make sure maximum amount of players at any time is 4
        if (numPlayers == 4) {
            numBots = 0;
        } else {
            numBots = getNumberBots(numPlayers);
        }

        makePlayerWonLost(numPlayers, numBots);

        boolean playAgain = true;
        while (playAgain) {
            playBlackJack(numPlayers , numBots);
            displayWonLost(numPlayers);
            playAgain = playAgain();
        }

    }
    //Displays number of times all players have won and lost
    private static void displayWonLost(int numPlayers){
        for (int i = 0; i < roundsWon.length; i++) {
            System.out.println("Player " + (i+1) + " has won: " + roundsWon[i] +
                    " times, lost: " + roundsLost[i] +
                    " times, and tied: " + roundsTied[i] + " times");
        }
    }

//Takes in number integer of players and number of bots
    // Initializes arrays for the int[] of numPlayers and numBots
private static void makePlayerWonLost(int numPlayers, int numBots) {
    roundsWon = new int[(numPlayers + numBots)];
    Arrays.fill(roundsWon, 0);

    roundsLost = new int[(numPlayers + numBots)];
    Arrays.fill(roundsLost, 0);

    roundsTied = new int[(numPlayers + numBots)];
    Arrays.fill(roundsTied, 0);
}

    /*Takes in no variables
    This method runs the game of blackjack calling other methods to play it out.
    Returns nothing.*/
    public static void playBlackJack(int numPlayers, int numBots){
        Dealer dealer = new Dealer();

        //Make deck and shuffle cards
        Deck gameDeck = new Deck();
        gameDeck.shuffleCards();
        //get a deck and shuffle
        Player[] players = createPlayers(numPlayers);
        Bot[] bots = createBots(numBots);

        //deal cards :)
        gameDeck = dealCards(players, bots, dealer, gameDeck);
        //players turns
        gameDeck = playerTurns(players, gameDeck, dealer);
        //bots turns
        gameDeck = botTurn(gameDeck, dealer, bots);
        //dealer turn
        gameDeck = dealerTurn(gameDeck, dealer);
        //Check who won
        checkWon(dealer, bots, players);
    }

    /*Methods for game operation*/

    /*Takes in no variables
    * Asks user if they will play again with*
    returns true if play again and false if not*/
    public static boolean playAgain() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Play Again? Type Yes");
        String answer = scanner.nextLine();
        answer = answer.substring(0, 1);//in case of mistype only checks for first letter
        if (answer.equalsIgnoreCase("y")) {
            return true;
        } else {
            return false;
        }

    }

    /*int numPlayers Positive number representing number of humans players to make
    Creates and initializes array of Players
    * Returns array of initialized players*/
    public static Player[] createPlayers(int numPlayers) {
        Player[] players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }
        return players;

    }

    /*int numBots Positive number representing number of bots to make
    Creates and initializes array of Bots
    Returns array of initialized Bots*/
    public static Bot[] createBots(int numBots) {
        Bot[] bots = new Bot[numBots];
        for (int i = 0; i < numBots; i++) {
            bots[i] = new Bot();
        }
        return bots;
    }

    /*Does not take in any variables
    Gets user to input number of players playing the game
    Returns int number of player (1-4)*/
    public static int getNumberPlayers() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("How many players will be playing? (Maximum of 4 players)");
        int x;
        int max_players = 4;

        while (true) {

            x = getIntInput();
            if (x > max_players || x <= 0) {
                System.out.println("Error: Between 1-4 Players");
            } else {
                return x;
            }
        }
    }

    /*Does not take in any variables
    Gets user to input number of bots playing the game
    Returns int number of bots (0-3)*/
    public static int getNumberBots(int numberPlayers) {

        System.out.println("How many bots will be playing? (Maximum of " + (4 - numberPlayers) + " bots)");
        int x;
        int max_bots = 4 - numberPlayers;

        while (true) {
            ;
            x = getIntInput();
            if (x > max_bots || x <= -1) {
                System.out.println("Error: Between 1-" + (4 - numberPlayers) + " bots");
            } else {
                return x;
            }
        }
    }

    /* Takes in dealer object , bots array of bot players and players array of human players for their hands
     * Determines and displays results of everyone's hands by comparing against the dealer
     * Returns nothing*/
    public static void checkWon(Dealer dealer, Bot[] bots, Player[] players) {
        int dealerValue = dealer.getHandValue(); // Get dealer hand value
        int playerNo = 1; // this is for display
        int playerNumber = 0;// this is for gamesWon
        boolean dealerBust = dealerValue > 21;
        System.out.println("\n\n===Final Results===");

        for (Player player : players) {
            int curPlayerValue = player.getHandValue();
            if (curPlayerValue > 21) { //player loses if greater than 21
                System.out.println(RED + "Player " + playerNo + " Busted" + RESET);
                roundsLost[playerNumber] += 1;
            } else if (dealerBust) { //Dealer busted? Player wins
                System.out.println(GREEN + "Player " + playerNo + " wins" + RESET);
                roundsWon[playerNumber] += 1;
            } else if (dealerValue < curPlayerValue) { // Dealer value less than current player Value? Player wins
                System.out.println(GREEN + "Player " + playerNo + " wins" + RESET);
                roundsWon[playerNumber] += 1;
            } else if (dealerValue == curPlayerValue) { // Dealer value = Player value? Push
                System.out.println(YELLOW + "Push for player " + playerNo + RESET);
                roundsTied[playerNumber] += 1;
            } else { // otherwise the player lost
                System.out.println(RED + "Player " + playerNo + " Lost" + RESET);
                roundsLost[playerNumber] += 1;
            }
            playerNo++;
            playerNumber++;
        }
        //label bots as 1 2 3  etc.

        for (Bot bot : bots) {
            int curBotValue = bot.getHandValue();
            if (curBotValue > 21) { //bot loses if greater than 21
                System.out.println(RED + "Bot " + playerNo + " Busted" + RESET);
                roundsLost[playerNumber] += 1;
            } else if (dealerBust) { //Dealer busted? Bot wins
                System.out.println(GREEN + "Bot " + playerNo + " wins" + RESET);
                roundsWon[playerNumber] += 1;
            } else if (dealerValue < curBotValue) {// Dealer value less than current Bot Value? Bot wins
                System.out.println(GREEN + "Bot " + playerNo + " wins" + RESET);
                roundsWon[playerNumber] += 1;
            } else if (dealerValue == curBotValue) { // Dealer value same as player value? Push
                System.out.println(YELLOW + "Push for bot " + playerNo + RESET);
                roundsTied[playerNumber] += 1;
            } else { //Otherwise bot lost
                System.out.println(RED + "Bot " + playerNo + " Lost" + RESET);
                roundsLost[playerNumber] += 1;
            }
            playerNo++;
            playerNumber++;
        }
    }

    /*Takes in array of PLayers, array of Bots, Dealer to receive cards and Deck to hand out the cards
    Deals initial cards to all players bots and one card to dealer as he has 1 hidden card
     Returns deck as deck will be smaller than before (having the missing cards)*/
    public static Deck dealCards(Player[] players, Bot[] bots, Dealer dealer, Deck deck) {
        System.out.println("Dealing...");
        //2 cards to each player
        for (Player player : players) {
            player.getCard(deck.drawCard());
            player.getCard(deck.drawCard());
        }
        // 2 cards to each bot
        for (Bot bot : bots) {
            bot.getCard(deck.drawCard());
            bot.getCard(deck.drawCard());
        }
        // 1 card for dealer
        dealer.getCard(deck.drawCard());//second card hidden
        return deck;

    }

    /*Takes in player array to have each player draw cards, Dealer to know the dealer up card, and deck to modify
    (remove old deck cards)
    This method plays out the players turns, asks when players want to hit, stand and displays if
    a player got blackjack or busted
    Returns modified deck as cards have been removed from it
    */
    public static Deck playerTurns(Player[] players, Deck deck, Dealer dealer) {
        Scanner scanner = new Scanner(System.in);
        int x = 0;
        // go through each player
        for (Player player : players) {
            x++;
            boolean standing = false;
            System.out.println("\n====Player " + x + "'s Turn=====");
            while (!standing && player.getHandValue() <= 21) { // While the player has not busted or isn't standing
                System.out.println("Dealer Visible Card:\n" + dealer.printHand() +
                        "Your Hand:\n" + player.printHand());

                if (player.getHandValue() == 21) { //If the player got blackjack
                    System.out.println("Blackjack!");
                    sleep(1000);
                    break;
                }
                System.out.println("Hit or Stand Type:(H/S)"); //This part figures out whether player wants to hit or stand
                String userInput = scanner.nextLine().toLowerCase().trim(); //makes sure player cant do typos
                if (!userInput.isEmpty()) {
                    userInput = userInput.substring(0,1);
                }
                while (!userInput.equals("h") && (!userInput.equals("s"))) {
                    System.out.println("H for hit S for stand");
                    userInput = scanner.nextLine().toLowerCase().trim();
                    if (!userInput.isEmpty()) {
                        userInput = userInput.substring(0,1);
                    }
                }
                if (userInput.equals("h")) { // if hit, draw a new card
                    Card card = deck.drawCard();
                    player.getCard(card);
                    int newHandValue = player.getHandValue();

                    if (player.getHandValue() > 21) { //if the player bust make sure to end loop
                        System.out.println("Your Hand:\n" + player.printHand()); // displays hand
                        sleep(2000);
                        System.out.println("Busted with:" + newHandValue); // displays value
                        sleep(2000);
                    }
                } else { // if "s" player stands displays hand value player stood with
                    System.out.println("Stand with: " + player.getHandValue());
                    standing = true;
                    sleep(2000);
                }
            }
        }
        return deck;
    }

    /*Takes in bot array to have each bot draw cards, Dealer to know the dealer up card, and deck to modify
    (remove old deck cards)
    This method plays out the bot turns, asks when bot want to hit, stand and displays if
    a player got blackjack or busted
    Returns modified deck as cards have been removed from it
    */
    public static Deck botTurn(Deck deck, Dealer dealer, Bot[] bots) {
        int botNumber = 0;
        for (Bot bot : bots) {// for each bot in the bot array
            botNumber++;
            int input = -1; // ensure that the while loop can be entered
            System.out.println("\n====Bot " + botNumber + "'s Turn=====");
            while (input != 0) {
                System.out.println("Dealer Visible Card:\n" + dealer.printHand() +
                        "Your Hand:\n" + bot.printHand()); // print bot hand
                input = bot.hitOrStand(dealer); // 1 - Hit 0 - Stand  -1 - Error (will redo loop)
                if (input == 1) {
                    System.out.println("Bot " + botNumber + " chose to hit.");
                    Card card = deck.drawCard();
                    bot.getCard(card);

                    sleep(3000);

                    if (bot.getHandValue() == 21) { // If blackjack say got blackjack
                        System.out.println("Your Hand:\n" + bot.printHand());

                        System.out.println("Bot " + botNumber + " got Blackjack!");
                        input = 0;
                        sleep(3000);
                    } else if (bot.getHandValue() > 21) { // If higher than 21 say busted
                        System.out.println("Your Hand:\n" + bot.printHand());
                        System.out.println("Bot " + botNumber + " Busted!");
                        input = 0;
                        sleep(3000);
                    }
                } else { // Stand
                    System.out.println("\n> Bot " + botNumber + " choose to Stand");
                    System.out.println("Final Total: " + bot.getHandValue());
                    input = 0; // this makes sure that its out of the loop
                    sleep(2500);
                }
            }

        }

        return deck;
    }

    /*Takes in Deck (remove cards) and Dealer (to modify its hand)
    This method plays out the dealers turn.
    Returns modified deck as cards have been removed from it
    */
    public static Deck dealerTurn(Deck deck, Dealer dealer) {
        System.out.println("\n====Dealer's Turn=====");
        dealer.getCard(deck.drawCard()); // get second card
        System.out.println("Dealer Cards:\n" + dealer.printHand()); //show hidden card
        sleep(2000);
        while (dealer.getHandValue() < 16) { //while below 16 (stands on 16)
            dealer.getCard(deck.drawCard()); // Draw card
            System.out.println("Dealer Cards:\n" + dealer.printHand()); // Print hand
            sleep(2000);
        }
        System.out.println("Dealer had: " + dealer.getHandValue()); // Get value from hand
        if (dealer.getHandValue() == 21) { // If dealer got 21
            System.out.println("\nDealer got Blackjack!");
        } else if (dealer.getHandValue() > 21) { // If dealer busted
            System.out.println("\nDealer bust!");
        }


        return deck;
    }

    /* Takes an integer
    Pause program in execution for the specified delay in milliseconds
    Doesn't return anything*/
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Log the interruption to error stream
            System.err.println("Sleep was interrupted");
        }
    }

    /* Gets an integer input from a user*/
    public static int getIntInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }
}


