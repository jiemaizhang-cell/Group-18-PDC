/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package blackjack;

/**
 *
 * @author 14432
 */
/**
 * Project by Jiemai Zhang 25294349 & Yaya Ge 25294047.
 * This is a project that allows the player to play Blackjack here.
 * Rules of Blackjack:1.Objective: Get as close to 21 as possible without going over.Beat the banker’s hand to win.
 * 2.Card Values: Cards 2–10 = face value. J, Q, K = 10. Ace (A) = 1 or 11, whichever is better for the hand.
 * 3.Dealing: Each player and the banker receive two cards. Players’ cards are face up. The banker has one card face up and one face down.
 * 4.Player’s Turn: The player can choose to take another card or keep your current hand. Continue until the player stands or goes over 21.
 * 5.Banker’s Turn: The banker shows the hidden card. The banker must draw cards until the total is 17 or higher and stop at 17 or more.
 * 6.Winning: If the player’s hand > 21 → blow up (lose). If the banker blows up and the player does not → Player wins. If both are under 21, the hand closer to 21 wins. If the totals are the same → tie.
 * 7.Blackjack: An Ace + 10-value card on the first two cards = Blackjack. Blackjack is the strongest hand.
 */
import java.util.*;

//class10-main class. Built by Jiemai and Yaya. to run the wohle game.
public class Blackjack {

    public static void main(String[] args) throws InterruptedException {
        String name;
        int rounds;
        int result;

        Scanner in = new Scanner(System.in);
        System.out.println("Please enter your name:");
        name = in.next();

        Record.readRecords(name);//show the past records.

        System.out.println("please enter how many rounds do you want to play:");
        rounds=in.nextInt();

        ScoreBoard scoreboard = new ScoreBoard();

        for (int i = 1; i <= rounds; i++) {
            System.out.println("Round " + i);
            GameRound round = new GameRound();
            result = round.playRound(in);
            if (result == 1) {
                scoreboard.whoWins("HumanPlayer");
            } else if (result == -1) {
                scoreboard.whoWins("Banker");
            }
        }

        scoreboard.finalResult(name);
        Record.saveRecord(name, scoreboard.HumanWins(), scoreboard.BankerWins());//record
    }
}

