/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

import java.util.ArrayList;

/**
 *
 * @author 14432
 */
//Built by Jiemai. initialize all players
public abstract class Player {

    public String name;
    ArrayList<Card> hands = new ArrayList<Card>();//cards that player has
    public int number = 0;
    public int index = 0;
    protected Deck deck;

    //Build the constructor
    public Player(Deck deck, String name) {
        this.deck = deck;
        this.name = name;
    }

    //for player to get the card
    public void getHandsCard() {
        hands.add(deck.deal());
    }

    public String showCard() {
        return hands.get(hands.size() - 1).toString();
    }

    //to get the value of each card
    public int cardValue(Card card) {
        String v = card.getValue();
        switch (v) {
            case "J":
            case "Q":
            case "K":
                return 10;
            case "A":
                return 11;
            default:
                return Integer.parseInt(v);
        }
    }

    //to calculate the value of all cards that palyer has
    public int countCard() {
        number = 0;
        for (Card c : hands) {
            number += cardValue(c);
        }
        return number;
    }

    //to deal the situation with "A"
    public boolean hasAce() {
        for (Card c : hands) {
            if (c.getValue().equals("A")) {
                c.value = "1";
                return true;
            }
        }
        return false;
    }

    public String getFullHandAsString() {

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < hands.size(); i++) {

            sb.append(hands.get(i).toString());

            if (i < hands.size() - 1) {

                sb.append("], [");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String getHiddenHandAsString() {

        if (hands.size() < 2) {
            return "[X]";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("[X], [");

        sb.append(hands.get(1).toString());

        sb.append("]");
        return sb.toString();
    }

    public java.util.List<Card> getHandList() {

        return this.hands;
    }

    public int getHandSize() {

        return hands.size();
    }

}
