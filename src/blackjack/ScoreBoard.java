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
    private String Winner="";

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
            Winner = "Both";
        } else if (humanScore > bankerScore) {
            System.out.println("You are the final winner!");
            Winner = name;
        } else {
            System.out.println("I edge you out!");
            Winner = "Banker";
        }
    }

    public int HumanWins() {
        return humanScore;
    }

    public int BankerWins() {
        return bankerScore;
    }
    
    public String Winner(){
        return Winner;
    }

    public String toString() {

        if (this.HumanWins() > this.BankerWins()) {
            return "You win! (" + this.HumanWins() + " vs " + this.BankerWins() + ")";
        } else if (this.BankerWins() > this.HumanWins()) {
            return "Banker wins! (" + this.BankerWins() + " vs " + this.HumanWins() + ")";
        } else {
            return "It's a draw! (" + this.HumanWins() + " vs " + this.BankerWins() + ")";
        }
        // ------------------------------------------
    }
}
