package com.tromza.pokertds.pokerLogic;

import java.util.ArrayList;

public class Deck {
    private final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};
    private final String[] SUITS = {"h", "d", "c", "s"};

    public ArrayList<String> get (){
        ArrayList<String> deck = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                deck.add(RANKS[i].concat(SUITS[j]));
            }
        }
        return deck;
    }

    public Deck() {
    }
}
