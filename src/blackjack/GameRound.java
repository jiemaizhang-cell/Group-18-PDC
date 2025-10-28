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

    //preparations before starting a game.
    public GameRound() {
        deck = new Deck();
        deck.shuffle();
        human = new HumanPlayer(deck);
        banker = new Banker(deck);
    }

    //every round
    public int playRound(Scanner in) throws InterruptedException {
        GameUtils.printLine();

        //Human player gets the first card.
        human.getHandsCard();
        System.out.println("The first card that you get is: " + human.showCard() + " (Current score: " + human.countCard() + ")");
        GameUtils.pause();

        //Banker gets the first card.
        banker.getHandsCard();
        System.out.println("The first card that I get is: " + banker.showCard() + " (Current score: " + banker.countCard() + ")");
        GameUtils.pause();

        //Human player gets the second card.
        human.getHandsCard();
        System.out.println("The second card that you get is: " + human.showCard() + " (Current score: " + human.countCard() + ")");
        GameUtils.pause();

        //Banker gets the second card and hides it.
        banker.getHandsCard();
        banker.countCard();

        //judge if someone gets the strongest hand--Blackjack!
        if (human.number == 21 && banker.number != 21) {
            System.out.println("Blackjack!!! you win!!!");
            return 1;
        } else if (banker.number == 21) {
            System.out.println("Let's see what my hidden card is. Blackjack!!! I win");
            return -1;
        }

        //Player's turn. Ask the player to add cards or keep the hand.
        while (human.number < 21) {
            System.out.println("Do you need more cards? yes=1, no=0(quit playing Blackjack=-1)");
            int flag = in.nextInt();
            if (flag == -1) {
                System.out.println("You choose to quit the game.");
                System.exit(0);
            }
            if (flag == 0) {
                break;
            }
            human.getHandsCard();
            System.out.println("The new card that you get is: " + human.showCard() + " (Current score: " + human.countCard() + ")");
            GameUtils.pause();

            //deal with the situation with Ace.
            if (human.number > 21 && human.hasAce()) {
                human.number -= 10;
                System.out.println("you can change your 'A' to '1'. New score: " + human.number);
                GameUtils.pause();
            }
        }
        //When the value>21, blow up->lose.
        if (human.number > 21) {
            System.out.println("OHNO you blew up!");
            GameUtils.pause();
            return -1;
        }

        //Banker's turn. draw cards until the total is 17 or higher and stop at 17 or more.
        System.out.println("The second card that I get is: " + banker.showCard() + " (Current score: " + banker.countCard() + ")");
        GameUtils.pause();
        while (banker.number < 17) {
            banker.getHandsCard();
            System.out.println("The new card that I get is: " + banker.showCard() + " (Current score: " + banker.countCard() + ")");
            GameUtils.pause();

            //deal with the situation with Ace.
            if (banker.number > 21 && banker.hasAce()) {
                banker.number -= 10;
                System.out.println("I change my 'A' to '1'. New score: " + banker.number);
                GameUtils.pause();
            }
        }

        //When the value>21, blow up->lose.
        if (banker.number > 21) {
            System.out.println("OHNO I blew up! Congratulations!!!");
            GameUtils.pause();
            return 1;
        }

        //compare the values of the hands.
        if (human.number == banker.number) {
            System.out.println("Hey we tied!");
            GameUtils.pause();
            return 0;
        } else if (human.number > banker.number) {
            System.out.println("You beat me this round!");
            GameUtils.pause();
            return 1;
        } else {
            System.out.println("I am a bit better this round~");
            GameUtils.pause();
            return -1;
        }
    }
}
