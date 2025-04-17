package project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer; // Explicitly import Swing Timer

public class BlackJack {
    private JFrame mainFrame;
    private JPanel gamePanel;
    private JPanel dealerPanel;
    private JPanel[] playerPanels;
    private JLabel statusLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private Player[] players;
    private Bot[] bots;
    private Dealer dealer;
    private Deck deck;
    private int currentPlayer;
    private boolean standing;
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private Point deckPosition;
    private Map<Card, Point> cardPositions = new HashMap<>();
    private Map<Card, Point> cardTargetPositions = new HashMap<>();
    private Timer animationTimer;

    public BlackJack() {
        mainFrame = new JFrame("BlackJack");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLayout(new BorderLayout());

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 80, 0), 0, getHeight(), new Color(0, 120, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gamePanel.setLayout(null);
        gamePanel.setBackground(new Color(0, 100, 0));

        dealerPanel = new JPanel();
        dealerPanel.setOpaque(false);
        dealerPanel.setBounds(350, 50, 300, 150);
        dealerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE), "Dealer", 0, 0, null, Color.WHITE));
        gamePanel.add(dealerPanel);

        playerPanels = new JPanel[4];
        for (int i = 0; i < 4; i++) {
            playerPanels[i] = new JPanel();
            playerPanels[i].setOpaque(false);
            int x = 50 + (i % 2) * 500;
            int y = 350 + (i / 2) * 150;
            playerPanels[i].setBounds(x, y, 400, 150);
            playerPanels[i].setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Player " + (i + 1), 0, 0, null, Color.WHITE));
            gamePanel.add(playerPanels[i]);
        }

        statusLabel = new JLabel("Welcome to BlackJack!", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setBounds(300, 300, 400, 50);
        gamePanel.add(statusLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(350, 600, 300, 50);
        hitButton = createStyledButton("Hit");
        standButton = createStyledButton("Stand");
        newGameButton = createStyledButton("New Game");
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
        gamePanel.add(buttonPanel);

        deckPosition = new Point(900, 50);
        mainFrame.add(gamePanel, BorderLayout.CENTER);

        startGame();
        mainFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(isEnabled() ? new Color(0, 150, 0) : Color.GRAY);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(Color.WHITE);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private void startGame() {
        int numPlayers = getNumberPlayers();
        players = createPlayers(numPlayers);
        bots = createBots(4 - numPlayers);
        playBlackJack();
    }

    private void playBlackJack() {
        dealer = new Dealer();
        deck = new Deck();
        deck.shuffleCards();
        dealerHand.clear();
        cardPositions.clear();
        cardTargetPositions.clear();

        // Deal initial cards with animation
        for (Player player : players) {
            animateCardDeal(player, deck.drawCard(), playerPanels[Arrays.asList(players).indexOf(player)]);
            animateCardDeal(player, deck.drawCard(), playerPanels[Arrays.asList(players).indexOf(player)]);
        }
        for (Bot bot : bots) {
            int index = players.length + Arrays.asList(bots).indexOf(bot);
            animateCardDeal(bot, deck.drawCard(), playerPanels[index]);
            animateCardDeal(bot, deck.drawCard(), playerPanels[index]);
        }
        dealerHand.add(deck.drawCard());
        dealer.getCard(dealerHand.get(0));
        animateCardDeal(dealer, dealerHand.get(0), dealerPanel);
        dealerHand.add(deck.drawCard());

        updateGUI();

        currentPlayer = 0;
        standing = false;
        if (currentPlayer < players.length) {
            setupPlayerTurn();
        } else {
            proceedToBotAndDealerTurns();
        }
    }

    private void animateCardDeal(AbstractPlayer player, Card card, JPanel targetPanel) {
        player.getCard(card);
        Point startPos = new Point(deckPosition);
        Point endPos = new Point(targetPanel.getX() + targetPanel.getComponentCount() * 90, targetPanel.getY() + 20);
        cardPositions.put(card, startPos);
        cardTargetPositions.put(card, endPos);

        if (animationTimer == null || !animationTimer.isRunning()) {
            animationTimer = new Timer(16, e -> {
                boolean stillAnimating = false;
                for (Map.Entry<Card, Point> entry : cardPositions.entrySet()) {
                    Card c = entry.getKey();
                    Point current = entry.getValue();
                    Point target = cardTargetPositions.get(c);
                    int dx = (target.x - current.x) / 5;
                    int dy = (target.y - current.y) / 5;
                    if (Math.abs(target.x - current.x) > 5 || Math.abs(target.y - current.y) > 5) {
                        current.x += dx;
                        current.y += dy;
                        stillAnimating = true;
                    } else {
                        current.setLocation(target);
                    }
                }
                updateGUI();
                if (!stillAnimating) {
                    ((Timer) e.getSource()).stop();
                }
            });
            animationTimer.start();
        }
    }

    private void setupPlayerTurn() {
        statusLabel.setText("Player " + (currentPlayer + 1) + "'s Turn - Hand Value: " + players[currentPlayer].getHandValue());
        for (int i = 0; i < playerPanels.length; i++) {
            playerPanels[i].setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(i == currentPlayer ? Color.YELLOW : Color.WHITE),
                "Player " + (i + 1), 0, 0, null, Color.WHITE));
        }

        if (players[currentPlayer].getHandValue() == 21) {
            statusLabel.setText("Player " + (currentPlayer + 1) + " has Blackjack!");
            currentPlayer++;
            proceedToNextTurn();
            return;
        }

        hitButton.setEnabled(true);
        standButton.setEnabled(true);

        hitButton.addActionListener(e -> {
            Card card = deck.drawCard();
            animateCardDeal(players[currentPlayer], card, playerPanels[currentPlayer]);
            statusLabel.setText("Player " + (currentPlayer + 1) + " drew: " + card.rank + " of " + card.suit +
                             " - New total: " + players[currentPlayer].getHandValue());

            if (players[currentPlayer].getHandValue() > 21) {
                statusLabel.setText("Player " + (currentPlayer + 1) + " Busted with: " + players[currentPlayer].getHandValue());
                hitButton.setEnabled(false);
                standButton.setEnabled(false);
                currentPlayer++;
                proceedToNextTurn();
            }
        });

        standButton.addActionListener(e -> {
            statusLabel.setText("Player " + (currentPlayer + 1) + " Stands with: " + players[currentPlayer].getHandValue());
            standing = true;
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            currentPlayer++;
            proceedToNextTurn();
        });
    }

    private void proceedToNextTurn() {
        if (currentPlayer < players.length && !standing) {
            setupPlayerTurn();
        } else {
            proceedToBotAndDealerTurns();
        }
    }

    private void proceedToBotAndDealerTurns() {
        for (int i = 0; i < bots.length; i++) {
            while (true) {
                int decision = bots[i].hitOrStand(dealer);
                if (decision == 1) {
                    Card card = deck.drawCard();
                    animateCardDeal(bots[i], card, playerPanels[players.length + i]);
                    if (bots[i].getHandValue() > 21) {
                        statusLabel.setText("Bot " + (i + 1) + " Busted!");
                        break;
                    }
                } else {
                    statusLabel.setText("Bot " + (i + 1) + " Stands with: " + bots[i].getHandValue());
                    break;
                }
            }
        }

        dealer.getCard(dealerHand.get(1));
        animateCardDeal(dealer, dealerHand.get(1), dealerPanel);

        while (dealer.getHandValue() < 16) {
            Card newCard = deck.drawCard();
            dealer.getCard(newCard);
            dealerHand.add(newCard);
            animateCardDeal(dealer, newCard, dealerPanel);
        }

        checkWon();
    }

    private void checkWon() {
        int dealerValue = dealer.getHandValue();
        StringBuilder results = new StringBuilder("===Final Results===\n");
        results.append("Dealer had: ").append(dealerValue).append("\n");
        boolean dealerBust = dealerValue > 21;

        int playerNo = 1;
        for (Player player : players) {
            int curPlayerValue = player.getHandValue();
            if (curPlayerValue > 21) {
                results.append("Player ").append(playerNo).append(" Busted\n");
            } else if (dealerBust) {
                results.append("Player ").append(playerNo).append(" wins\n");
            } else if (dealerValue < curPlayerValue) {
                results.append("Player ").append(playerNo).append(" wins\n");
            } else if (dealerValue == curPlayerValue) {
                results.append("Push for player ").append(playerNo).append("\n");
            } else {
                results.append("Player ").append(playerNo).append(" Lost\n");
            }
            playerNo++;
        }

        for (Bot bot : bots) {
            int curPlayerValue = bot.getHandValue();
            if (curPlayerValue > 21) {
                results.append("Bot ").append(playerNo).append(" Busted\n");
            } else if (dealerBust) {
                results.append("Bot ").append(playerNo).append(" wins\n");
            } else if (dealerValue < curPlayerValue) {
                results.append("Bot ").append(playerNo).append(" wins\n");
            } else if (dealerValue == curPlayerValue) {
                results.append("Push for bot ").append(playerNo).append("\n");
            } else {
                results.append("Bot ").append(playerNo).append(" Lost\n");
            }
            playerNo++;
        }

        statusLabel.setText("<html>" + results.toString().replace("\n", "<br>") + "</html>");

        int choice = JOptionPane.showConfirmDialog(mainFrame, results.toString() + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        dealerPanel.removeAll();
        for (JPanel panel : playerPanels) {
            panel.removeAll();
            panel.setVisible(true);
        }
        startGame();
    }

    private void updateGUI() {
        dealerPanel.removeAll();
        for (JPanel panel : playerPanels) {
            panel.removeAll();
        }

        if (currentPlayer < players.length || standing) {
            dealerPanel.add(createCardPanel(dealerHand.get(0), false));
            dealerPanel.add(createCardPanel(null, true));
        } else {
            for (Card card : dealerHand) {
                dealerPanel.add(createCardPanel(card, false));
            }
        }

        for (int i = 0; i < players.length; i++) {
            playerPanels[i].setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(i == currentPlayer ? Color.YELLOW : Color.WHITE),
                "Player " + (i + 1) + " (Value: " + players[i].getHandValue() + ")", 0, 0, null, Color.WHITE));
            for (Card card : players[i].hand) {
                playerPanels[i].add(createCardPanel(card, false));
            }
        }

        for (int i = 0; i < bots.length; i++) {
            playerPanels[i + players.length].setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Bot " + (i + 1) + " (Value: " + bots[i].getHandValue() + ")", 0, 0, null, Color.WHITE));
            for (Card card : bots[i].hand) {
                playerPanels[i + players.length].add(createCardPanel(card, false));
            }
        }

        for (int i = players.length + bots.length; i < 4; i++) {
            playerPanels[i].setVisible(false);
        }

        for (Map.Entry<Card, Point> entry : cardPositions.entrySet()) {
            Card card = entry.getKey();
            Point pos = entry.getValue();
            JPanel cardPanel = createCardPanel(card, false);
            cardPanel.setBounds(pos.x, pos.y, 80, 120);
            gamePanel.add(cardPanel);
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private JPanel createCardPanel(Card card, boolean hidden) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hidden) {
                    g2d.setColor(new Color(0, 0, 100));
                    g2d.fillRoundRect(0, 0, 80, 120, 10, 10);
                    g2d.setColor(Color.WHITE);
                    g2d.drawRoundRect(0, 0, 80, 120, 10, 10);
                } else if (card != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(0, 0, 80, 120, 10, 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRoundRect(0, 0, 80, 120, 10, 10);

                    if (card.suit.equals("Hearts") || card.suit.equals("Diamonds")) {
                        g2d.setColor(Color.RED);
                    } else {
                        g2d.setColor(Color.BLACK);
                    }

                    g2d.setFont(new Font("Arial", Font.BOLD, 20));
                    String suitSymbol = card.suit.equals("Hearts") ? "♥" :
                                      card.suit.equals("Diamonds") ? "♦" :
                                      card.suit.equals("Clubs") ? "♣" : "♠";
                    String rank = card.rank.equals("10") ? "10" : card.rank.substring(0, 1);
                    g2d.drawString(rank, 10, 25);
                    g2d.drawString(suitSymbol, 10, 50);
                    g2d.drawString(suitSymbol, 50, 100);
                }
            }
        };
        panel.setPreferredSize(new Dimension(80, 120));
        panel.setOpaque(false);
        return panel;
    }

    private int getNumberPlayers() {
        String input = JOptionPane.showInputDialog(mainFrame, "How many players (1-4)?");
        int x;
        int maxPlayers = 4;
        try {
            x = Integer.parseInt(input);
            if (x > maxPlayers || x <= 0) {
                JOptionPane.showMessageDialog(mainFrame, "Error: Between 1-4 Players");
                return getNumberPlayers();
            }
            statusLabel.setText("Players: " + x + " Bots: " + (4 - x));
            return x;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Please enter a valid number");
            return getNumberPlayers();
        }
    }

    private Player[] createPlayers(int numPlayers) {
        Player[] players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }
        return players;
    }

    private Bot[] createBots(int numBots) {
        Bot[] bots = new Bot[numBots];
        for (int i = 0; i < numBots; i++) {
            bots[i] = new Bot();
        }
        return bots;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackJack());
    }
}