/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package blackjack;
// build by jiemai-- main game screem

import Database.database;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.CardLayout;
import Database.recordData;
import javax.swing.ImageIcon;
import java.awt.Image;

public class GamePanel extends javax.swing.JPanel {

    private final JPanel mainPanelContainer;
    private final CardLayout cardLayout;
    private final GameContext context;

    private GameRound currentRoundLogic;

    public GamePanel(JPanel mainPanel, CardLayout cardLayout, GameContext context) {

        this.mainPanelContainer = mainPanel;
        this.cardLayout = cardLayout;
        this.context = context;

        initComponents();

        bankerCardPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        playerCardPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        hitButton.addActionListener(e -> onHit());
        standButton.addActionListener(e -> onStand());
        nextRoundButton.addActionListener(e -> startNewRound());
        backToMenuButton.addActionListener(e -> onBackToMenu());

        nextRoundButton.setVisible(false);
        backToMenuButton.setVisible(true);

    }

    public void startGame() {

        context.resetGame();

        String name = context.getPlayerName();
        messageLabel.setText("欢迎, " + name + "! 祝你好运。");

        //record the player's name
        recordData.insertLoginRecord(name);

        startNewRound();
    }

    private void startNewRound() {

        updateMatchScoreLabel();

        if (context.getCurrentRound() > context.getTotalRounds()) {
            endGame();
            return;
        }

        currentRoundLogic = new GameRound();
        currentRoundLogic.startRound();

        roundInfoLabel.setText("Round: " + context.getCurrentRound() + " of " + context.getTotalRounds());
        updateCardImages(false);
        updateCardLabels(false);

        messageLabel.setText("Your turn: Hit or Stand?");
        hitButton.setVisible(true);
        standButton.setVisible(true);
        nextRoundButton.setVisible(false);
        backToMenuButton.setVisible(true);
    }

    private void endRound(int result) {

        String winner;
        switch (result) {
            case 1 -> {
                context.getScoreboard().whoWins("HumanPlayer");
                messageLabel.setText("You win");
                winner = "Player";
            }
            case -1 -> {
                context.getScoreboard().whoWins("Banker");
                messageLabel.setText("You lost");
                winner = "Banker";
            }
            default -> {
                messageLabel.setText("We tie");
                winner = "Both";
            }
        }

        //record every round
        recordData.insertRoundRecord(context.getPlayerName(), context.getCurrentRound(), currentRoundLogic.getPlayerScore(), currentRoundLogic.getBankerScore(), winner);

        context.incrementRound();

        updateMatchScoreLabel();

        hitButton.setVisible(false);
        standButton.setVisible(false);
        nextRoundButton.setVisible(true);
        backToMenuButton.setVisible(true);
    }

    private void endGame() {

        context.getScoreboard().finalResult(context.getPlayerName());
        //R-E-C-O-R-D, write
        Record.saveRecord(context.getPlayerName(),
                context.getScoreboard().HumanWins(),
                context.getScoreboard().BankerWins());
        recordData.insertRecord(context.getPlayerName(), context.getTotalRounds(), context.getScoreboard().HumanWins(), context.getScoreboard().BankerWins(), context.getScoreboard().Winner());
        //read read read
        recordData.showAllRecords();
        recordData.showPlayerHistory(context.getPlayerName());
        recordData.showTopPlayers();
        //shut down database
        database.closeConnection();

        messageLabel.setText("Game over, the result is" + context.getScoreboard().toString());

        hitButton.setVisible(false);
        standButton.setVisible(false);
        nextRoundButton.setVisible(false);
        backToMenuButton.setVisible(true);
    }

    private void onHit() {
        boolean isBust = currentRoundLogic.playerHits();

        if (isBust) {

            messageLabel.setText("You lost, Banker win");

            updateCardImages(true);
            updateCardLabels(true);

            endRound(-1);
        } else {

            updateCardImages(false);
            updateCardLabels(false);

            messageLabel.setText("Hit or Stand?");
        }
    }

    private void onStand() {
        hitButton.setVisible(false);
        standButton.setVisible(false);

        int result = currentRoundLogic.playerStands();

        updateCardImages(true);
        updateCardLabels(true);

        endRound(result);
    }

    private void onBackToMenu() {

        context.resetGame();
        cardLayout.show(mainPanelContainer, "MENU");
    }

    private void updateCardImages(boolean showAllDealerCards) {

        playerCardPanel.removeAll();
        bankerCardPanel.removeAll();

        java.util.List<Card> playerCards = currentRoundLogic.getPlayerHand();
        java.util.List<Card> bankerCards = currentRoundLogic.getBankerHand();

        int cardWidth = 90;
        int cardHeight = 125;

        try {
            for (Card card : playerCards) {
                String suit = card.getSuit().toString().toLowerCase();
                String rank = card.getValue().toString();
                String imagePath = "cards/" + suit + "_" + rank + ".jpg";

                java.net.URL imgURL = getClass().getResource(imagePath);

                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);

                    Image image = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(image);

                    playerCardPanel.add(new JLabel(scaledIcon));
                } else {
                    System.err.println("can not find picture: " + imagePath);
                    playerCardPanel.add(new JLabel("[" + card.toString() + "]"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            playerCardPanel.add(new JLabel("Error occurred while loading the player's cards."));
        }

        try {
            String cardBackPath = "cards/card_back.jpg";
            java.net.URL cardBackURL = getClass().getResource(cardBackPath);
            ImageIcon cardBackIcon = null;

            if (cardBackURL != null) {
                ImageIcon originalIcon = new ImageIcon(cardBackURL);
                Image image = originalIcon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                cardBackIcon = new ImageIcon(image);
            }

            for (int i = 0; i < bankerCards.size(); i++) {
                if (i == 0 && !showAllDealerCards) {

                    if (cardBackIcon != null) {
                        bankerCardPanel.add(new JLabel(cardBackIcon));
                    } else {
                        bankerCardPanel.add(new JLabel("[X]"));
                    }
                } else {
                    Card card = bankerCards.get(i);
                    String suit = card.getSuit().toString().toLowerCase();
                    String rank = card.getValue().toString();
                    String imagePath = "cards/" + suit + "_" + rank + ".jpg";
                    java.net.URL imgURL = getClass().getResource(imagePath);

                    if (imgURL != null) {
                        ImageIcon icon = new ImageIcon(imgURL);

                        Image image = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(image);

                        bankerCardPanel.add(new JLabel(scaledIcon));
                    } else {
                        bankerCardPanel.add(new JLabel("[" + card.toString() + "]"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankerCardPanel.add(new JLabel("Error occurred while loading the dealer's cards."));
        }

        playerCardPanel.revalidate();
        playerCardPanel.repaint();

        bankerCardPanel.revalidate();
        bankerCardPanel.repaint();
    }

    private void updateCardLabels(boolean showAllDealerCards) {

        playerScoreLabel.setText("Score: " + currentRoundLogic.getPlayerScore());

        if (showAllDealerCards) {

            bankerScoreLabel.setText("Score: " + currentRoundLogic.getBankerScore());
        } else {

            bankerScoreLabel.setText("Score: ?");
        }
    }

    private void updateMatchScoreLabel() {

        int playerWins = context.getScoreboard().HumanWins();
        int bankerWins = context.getScoreboard().BankerWins();

        matchScoreLabel.setText("Match Score: Player " + playerWins + " - Banker " + bankerWins);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BankerCardsLabel = new javax.swing.JLabel();
        playerCardsLabel = new javax.swing.JLabel();
        messageLabel = new javax.swing.JLabel();
        roundInfoLabel = new javax.swing.JLabel();
        hitButton = new javax.swing.JButton();
        standButton = new javax.swing.JButton();
        nextRoundButton = new javax.swing.JButton();
        backToMenuButton = new javax.swing.JButton();
        bankerCardPanel = new javax.swing.JPanel();
        playerCardPanel = new javax.swing.JPanel();
        playerScoreLabel = new javax.swing.JLabel();
        bankerScoreLabel = new javax.swing.JLabel();
        matchScoreLabel = new javax.swing.JLabel();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        messageLabel.setText("messageLabel");

        roundInfoLabel.setText("roundInfoLabel");

        hitButton.setText("hitButton");
        hitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitButtonActionPerformed(evt);
            }
        });

        standButton.setText("standButton");
        standButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standButtonActionPerformed(evt);
            }
        });

        nextRoundButton.setText("nextRoundButton");
        nextRoundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextRoundButtonActionPerformed(evt);
            }
        });

        backToMenuButton.setText("backToMenuButton");
        backToMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMenuButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bankerCardPanelLayout = new javax.swing.GroupLayout(bankerCardPanel);
        bankerCardPanel.setLayout(bankerCardPanelLayout);
        bankerCardPanelLayout.setHorizontalGroup(
            bankerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        bankerCardPanelLayout.setVerticalGroup(
            bankerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout playerCardPanelLayout = new javax.swing.GroupLayout(playerCardPanel);
        playerCardPanel.setLayout(playerCardPanelLayout);
        playerCardPanelLayout.setHorizontalGroup(
            playerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        playerCardPanelLayout.setVerticalGroup(
            playerCardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        playerScoreLabel.setText("Score: 0");

        bankerScoreLabel.setText("Score: ?");

        matchScoreLabel.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 18)); // NOI18N
        matchScoreLabel.setText("Match Score: Player 0 - Banker 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(BankerCardsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bankerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(bankerScoreLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(playerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerCardsLabel)
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(playerScoreLabel)
                        .addGap(76, 76, 76))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(matchScoreLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(nextRoundButton))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(messageLabel)
                                    .addGap(102, 102, 102))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(hitButton)
                                    .addGap(39, 39, 39)
                                    .addComponent(standButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))))
                .addContainerGap(93, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(roundInfoLabel)
                        .addGap(51, 51, 51))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backToMenuButton)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(BankerCardsLabel)
                            .addComponent(playerCardsLabel))
                        .addGap(97, 97, 97))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bankerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bankerScoreLabel)
                    .addComponent(playerScoreLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundInfoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(matchScoreLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(messageLabel)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hitButton)
                    .addComponent(standButton))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backToMenuButton)
                    .addComponent(nextRoundButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void hitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hitButtonActionPerformed

    private void standButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_standButtonActionPerformed

    private void nextRoundButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextRoundButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nextRoundButtonActionPerformed

    private void backToMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMenuButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_backToMenuButtonActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        startNewRound();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BankerCardsLabel;
    private javax.swing.JButton backToMenuButton;
    private javax.swing.JPanel bankerCardPanel;
    private javax.swing.JLabel bankerScoreLabel;
    private javax.swing.JButton hitButton;
    private javax.swing.JLabel matchScoreLabel;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton nextRoundButton;
    private javax.swing.JPanel playerCardPanel;
    private javax.swing.JLabel playerCardsLabel;
    private javax.swing.JLabel playerScoreLabel;
    private javax.swing.JLabel roundInfoLabel;
    private javax.swing.JButton standButton;
    // End of variables declaration//GEN-END:variables
}
