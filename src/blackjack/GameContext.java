/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

// build by Jiemai --to Transmit and store the game's state
public class GameContext {

    private String playerName;
    private int totalRounds;
    private int currentRound = 1;
    private ScoreBoard scoreboard;

    public GameContext() {
        this.scoreboard = new ScoreBoard();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int rounds) {
        this.totalRounds = rounds;
    }

    public ScoreBoard getScoreboard() {
        return scoreboard;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void incrementRound() {
        this.currentRound++;
    }

    public void resetGame() {
        this.playerName = "";
        this.totalRounds = 0;
        this.currentRound = 1;
        this.scoreboard = new ScoreBoard();
    }
}
