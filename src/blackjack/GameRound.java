/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

import java.util.Scanner;

/**
 *
 * @author 14432
 */
//class9-GameRound Built by Yaya and Jiemai. We seperate the game round from the main class to make the main class more clear.
public class GameRound {

    private final Player human;
    private final Player banker;
    private final Deck deck;

    public GameRound() {
        deck = new Deck();
        deck.shuffle();
        human = new HumanPlayer(deck);
        banker = new Banker(deck);
    }

    public void startRound() {
        human.getHandsCard();
        human.countCard();

        banker.getHandsCard();
        banker.countCard();

        human.getHandsCard();
        human.countCard();

        banker.getHandsCard();
        banker.countCard();
    }

    public int checkInitialBlackjack() {
        if (human.number == 21 && banker.number != 21) {
            return 1;
        } else if (banker.number == 21) {
            return -1;
        }
        return 0;
    }

    public boolean playerHits() {
        human.getHandsCard();
        human.countCard();

        if (human.number > 21 && human.hasAce()) {
            human.number -= 10;
        }

        return human.number > 21;
    }

    public int playerStands() {

        int playerTotal = human.number;
        int bankerTotal = banker.number;

        boolean playerHasBlackjack = (playerTotal == 21 && human.getHandSize() == 2);
        boolean bankerHasBlackjack = (bankerTotal == 21 && banker.getHandSize() == 2);

        if (playerHasBlackjack) {
            if (bankerHasBlackjack) {
                return 0;
            } else {
                return 1;
            }
        }

        if (bankerHasBlackjack) {
            return -1;
        }

        while (banker.number < 17) {
            banker.getHandsCard();
            banker.countCard();

            if (banker.number > 21 && banker.hasAce()) {
                banker.number -= 10;
            }
        }

        int finalBankerTotal = banker.number;

        if (finalBankerTotal > 21) {
            return 1;
        }

        if (playerTotal == finalBankerTotal) {
            return 0;
        } else if (playerTotal > finalBankerTotal) {
            return 1;
        } else {
            return -1;
        }
    }

    public String getPlayerHandDescription() {
        return human.getFullHandAsString() + " (Score: " + human.number + ")";
    }

    public String getBankerHandDescription(boolean showAll) {
        if (showAll) {
            return banker.getFullHandAsString() + " (Score: " + banker.number + ")";
        } else {
            return banker.getHiddenHandAsString() + " (Score: ?)";
        }
    }

    public java.util.List<Card> getPlayerHand() {

        return this.human.getHandList();
    }

    public java.util.List<Card> getBankerHand() {

        return this.banker.getHandList();
    }

    public int getPlayerScore() {
        return human.number;
    }

    public int getBankerScore() {
        return banker.number;
    }

}
