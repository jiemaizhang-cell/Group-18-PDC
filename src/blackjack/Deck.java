/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author 14432
 */
public class Deck {

    ArrayList<Card> cards = new ArrayList<Card>();
    public int index = 0;

    String[] suits = {"spades", "hearts", "diamonds", "clubs"};
    String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    //Build the constructor
    public Deck() {
        for (String suit : suits) {
            for (String value : values) {
                cards.add(new Card(suit, value));
            }
        }
    }

    // to shuffle the card
    public void shuffle() {
        Collections.shuffle(cards);
        index = 0;
    }

    // to get the card
    public Card deal() {
        if (index >= cards.size()) {
            throw new IllegalStateException("The cards have been dealt out.");
        } else {
            return cards.get(index++);
        }
    }

}
