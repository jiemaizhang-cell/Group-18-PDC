/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author 14432
 */
// Built by Jiemai. to get the card
public class Card {

    String suit;
    String value;

    //build the constructor
    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    // output the card
    @Override
    public String toString() {
        return suit + value;
    }
}
