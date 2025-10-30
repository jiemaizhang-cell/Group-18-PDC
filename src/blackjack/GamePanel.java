/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package blackjack;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Database.database;
import Database.recordData;
import javax.swing.ImageIcon;
import java.awt.Image;

public class GamePanel extends javax.swing.JPanel {

    private JPanel mainPanelContainer;
    private CardLayout cardLayout;
    private GameContext context;

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
        backToMenuButton.setVisible(false);

    }

    public void startGame() {

        context.resetGame();

        String name = context.getPlayerName();
        messageLabel.setText("欢迎, " + name + "! 祝你好运。");

        startNewRound();
    }

    private void startNewRound() {

        if (context.getCurrentRound() > context.getTotalRounds()) {
            endGame();
            return;
        }

        currentRoundLogic = new GameRound();

        currentRoundLogic.startRound();

        roundInfoLabel.setText("Round: " + context.getCurrentRound() + " of " + context.getTotalRounds());

        updateCardImages(false);

        int blackjackResult = currentRoundLogic.checkInitialBlackjack();

        if (blackjackResult != 0) {

            if (blackjackResult == 1) {
                messageLabel.setText("BLACKJACK! You win this round!");
            } else {
                messageLabel.setText("Banker has BLACKJACK! You lose.");
            }
            endRound(blackjackResult);
        } else {

            messageLabel.setText("Your turn: Hit or Stand?");
            hitButton.setVisible(true);
            standButton.setVisible(true);
            nextRoundButton.setVisible(false);
            backToMenuButton.setVisible(false);
        }
    }

    private void endRound(int result) {

        if (result == 1) {
            context.getScoreboard().whoWins("HumanPlayer");
            messageLabel.setText("You win");
        } else if (result == -1) {
            context.getScoreboard().whoWins("Banker");
            messageLabel.setText("You lost");
        } else {
            messageLabel.setText("We tie");
        }

        context.incrementRound();

        hitButton.setVisible(false);
        standButton.setVisible(false);
        nextRoundButton.setVisible(true);
        backToMenuButton.setVisible(false);
    }

    private void endGame() {

        context.getScoreboard().finalResult(context.getPlayerName());
        Record.saveRecord(context.getPlayerName(),
                context.getScoreboard().HumanWins(),
                context.getScoreboard().BankerWins());
        recordData.insertRecord(context.getPlayerName(), context.getTotalRounds(), context.getScoreboard().HumanWins(), context.getScoreboard().BankerWins(), context.getScoreboard().Winner());
        database.closeConnection();

        messageLabel.setText("Game over, the result is" + context.getScoreboard().toString());

        hitButton.setVisible(false);
        standButton.setVisible(false);
        nextRoundButton.setVisible(false);
        backToMenuButton.setVisible(true);
    }

    private void onHit() {
        boolean isBust = currentRoundLogic.playerHits();

        updateCardImages(false);

        if (isBust) {
            messageLabel.setText("You lost, Banker win");
            endRound(-1);
        } else {
            messageLabel.setText("Hit or Stand?");
        }
    }

    private void onStand() {
        hitButton.setVisible(false);
        standButton.setVisible(false);

        int result = currentRoundLogic.playerStands();

        updateCardImages(true);

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
                    // 显示明牌
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(BankerCardsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageLabel)
                    .addComponent(bankerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(playerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerCardsLabel))
                    .addComponent(roundInfoLabel))
                .addGap(59, 59, 59))
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextRoundButton)
                    .addComponent(hitButton))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(standButton)
                    .addComponent(backToMenuButton))
                .addContainerGap(36, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageLabel)
                    .addComponent(roundInfoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hitButton)
                    .addComponent(standButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextRoundButton)
                    .addComponent(backToMenuButton))
                .addGap(13, 13, 13))
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
    private javax.swing.JButton hitButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton nextRoundButton;
    private javax.swing.JPanel playerCardPanel;
    private javax.swing.JLabel playerCardsLabel;
    private javax.swing.JLabel roundInfoLabel;
    private javax.swing.JButton standButton;
    // End of variables declaration//GEN-END:variables
}
