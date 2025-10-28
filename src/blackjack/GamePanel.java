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
        
       
        updateCardLabels(false); 
        
      
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
        
      
        messageLabel.setText("Game over, the result is" + context.getScoreboard().toString()); 
        
        
        hitButton.setVisible(false);
        standButton.setVisible(false);
        nextRoundButton.setVisible(false);
        backToMenuButton.setVisible(true);
    }
    
   
    private void onHit() {
        boolean isBust = currentRoundLogic.playerHits(); 
        
        updateCardLabels(false);
        
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
        
        updateCardLabels(true); 
        
        endRound(result);
    }
    
   
    private void onBackToMenu() {
      
        context.resetGame(); 
        cardLayout.show(mainPanelContainer, "MENU");
    }

  
   private void updateCardLabels(boolean showAllDealerCards) {
        playerCardsLabel.setText("Player cards: " + currentRoundLogic.getPlayerHandDescription());
        BankerCardsLabel.setText("Banker cards: " + currentRoundLogic.getBankerHandDescription(showAllDealerCards));
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

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        BankerCardsLabel.setText("BankerCardsLabel");

        playerCardsLabel.setText("playerCardsLabel");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageLabel)
                        .addGap(151, 151, 151)
                        .addComponent(roundInfoLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BankerCardsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(playerCardsLabel)
                        .addGap(72, 72, 72))))
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
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BankerCardsLabel)
                    .addComponent(playerCardsLabel))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageLabel)
                    .addComponent(roundInfoLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
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
    private javax.swing.JButton hitButton;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JButton nextRoundButton;
    private javax.swing.JLabel playerCardsLabel;
    private javax.swing.JLabel roundInfoLabel;
    private javax.swing.JButton standButton;
    // End of variables declaration//GEN-END:variables
}
