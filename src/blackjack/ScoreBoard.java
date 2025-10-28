/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author 14432
 */
//class7-ScoreBoard, Built by Yaya. to analize who to win. It can help with showing final results conveniently.
public class ScoreBoard {

    private int humanScore = 0;
    private int bankerScore = 0;

    //in every round
    public void whoWins(String winner) {
        if (winner.equals("HumanPlayer")) {
            humanScore++;
        } else if (winner.equals("Banker")) {
            bankerScore++;
        }
    }

    //final result
    public void finalResult(String name) {
        System.out.println("The final Result is:");
        System.out.println(name + " " + humanScore + " : " + bankerScore);
        if (humanScore == bankerScore) {
            System.out.println("Tie overall!");
        } else if (humanScore > bankerScore) {
            System.out.println("You are the final winner!");
        } else {
            System.out.println("I edge you out!");
        }
    }

    public int HumanWins() {
        return humanScore;
    }

    public int BankerWins() {
        return bankerScore;
    }
}
