package com.tromza.pokertds.gamesLogic.pokerLogic;

import java.util.ArrayList;
import java.util.Random;

public class PokerGame {
    public static ArrayList<String> getPreflops() {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        Random random = new Random();
        String playerHand = "";
        String ourHand = "";
        for (int i = 0; i < 2; i++) {
            String card = thisDeck.get(random.nextInt(52 - i * 2));
            playerHand = playerHand.concat(card);
            thisDeck.remove(card);
            card = thisDeck.get(random.nextInt(52 - i * 2 - 1));
            ourHand = ourHand.concat(card);
            thisDeck.remove(card);
        }
        ArrayList<String> preflops = new ArrayList<>();
        preflops.add(playerHand);
        preflops.add(ourHand);
        return preflops;
    }

    public static String getFlop(String[] casinoPreflop, String[] playerPreflop) {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        Random random = new Random();
        String flop = "";
        for (int i = 0; i < 2; i++) {
            thisDeck.remove(casinoPreflop[i]);
            thisDeck.remove(playerPreflop[i]);
        }
        String card;
        for (int i = 0; i < 3; i++) {
            card = thisDeck.get(random.nextInt(thisDeck.size() - i));
            flop = flop.concat(card);
            thisDeck.remove(card);
        }
        return flop;
    }

    public static String getTern(String[] casinoPreflop, String[] playerPreflop, String[] board) {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            thisDeck.remove(casinoPreflop[i]);
            thisDeck.remove(playerPreflop[i]);
            thisDeck.remove(board[i]);
        }
        thisDeck.remove(board[2]);
        return thisDeck.get(random.nextInt(thisDeck.size()));
    }

    public static String getRiver(String[] casinoPreflop, String[] playerPreflop, String[] board) {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            thisDeck.remove(casinoPreflop[i]);
            thisDeck.remove(playerPreflop[i]);
            thisDeck.remove(board[i]);
            thisDeck.remove(board[i + 2]);
        }
        return thisDeck.get(random.nextInt(thisDeck.size()));
    }
}
