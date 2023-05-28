package com.tromza.pokertds.pokerLogic;

import com.tromza.pokertds.gamesLogic.pokerLogic.Chanses;
import com.tromza.pokertds.gamesLogic.pokerLogic.Deck;
import com.tromza.pokertds.gamesLogic.pokerLogic.FiveCardDraw;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class EvalSpeedTest {
    @Test
    public void processAllCombinations() {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        String[] deckArr = new String[thisDeck.size()];
        thisDeck.toArray(deckArr);
        String combination;
        int[] arr = null;
        while ((arr = Chanses.genCombinations(arr, 5, thisDeck.size())) != null) {
            combination = deckArr[(arr[0] - 1)] + deckArr[(arr[1] - 1)] + deckArr[(arr[2] - 1)] + deckArr[(arr[3] - 1)] + deckArr[(arr[4] - 1)];
            FiveCardDraw.powerOfCards(combination);
        }
    }
}
