/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class BlackjackGUI {
    public static void main(String[] args) {
       
        JFrame mainFrame = new JFrame("Blackjack");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        GameContext sharedContext = new GameContext();

        
        MainMenuPanel menuScreen = new MainMenuPanel(mainPanel, cardLayout, sharedContext);
        GamePanel gameScreen = new GamePanel(mainPanel, cardLayout, sharedContext); 

       
        mainPanel.add(menuScreen, "MENU");
        mainPanel.add(gameScreen, "GAME");

        
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        cardLayout.show(mainPanel, "MENU");
        
       
    }
}
