package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.TexasHoldemGame;
import com.tromza.pokertds.gamesLogic.pokerLogic.Chanses;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TexasHoldemServiceTest {

    private final TexasHoldemGame texasHoldemGame = new TexasHoldemGame();

    private final Pattern card = Pattern.compile("[2-9,TJQKA][hcds]");

    @Test
    public void playTexasHoldemFullHouseQQQ88vs888KK() throws Exception {
        texasHoldemGame.setPlayerPreflop("QhQd");
        texasHoldemGame.setCasinoPreflop("Kh8h");
        texasHoldemGame.setFlop("Qs8dKd");
        texasHoldemGame.setTern("2s");
        texasHoldemGame.setRiver("8c");
        Matcher ourHandMatcher = card.matcher(texasHoldemGame.getCasinoPreflop());
        String[] ourHand = new String[2];
        int i = 0;
        while (ourHandMatcher.find()) {
            ourHand[i] = ourHandMatcher.group();
            i++;
        }
        Matcher playerHandMatcher = card.matcher(texasHoldemGame.getPlayerPreflop());
        String[] playerHand = new String[2];
        i = 0;
        while (playerHandMatcher.find()) {
            playerHand[i] = playerHandMatcher.group();
            i++;
        }
        Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
        String[] board = new String[5];
        i = 0;
        while (boardMatcher.find()) {
            board[i] = boardMatcher.group();
            i++;
        }
        board[3] = texasHoldemGame.getTern();
        board[4] = texasHoldemGame.getRiver();
        Map.Entry<String, Double> casinoResult = Chanses.evalCombinationByHandAndBoard(ourHand, board);
        Map.Entry<String, Double> playerResult = Chanses.evalCombinationByHandAndBoard(playerHand, board);
        double resultCasino = casinoResult.getValue();
        String bestCombCasino = casinoResult.getKey();
        double resultPlayer = playerResult.getValue();
        String bestCombPlayer = playerResult.getKey();
        assertArrayEquals(new String[]{"Kh", "8h"}, ourHand);
        assertArrayEquals(new String[]{"Qh", "Qd"}, playerHand);
        assertArrayEquals(new String[]{"Qs", "8d", "Kd", "2s", "8c"}, board);
        assertTrue(resultPlayer > resultCasino);
        assertEquals(5, bestCombPlayer.chars().filter(x -> x == 'Q' || x == '8').count());
        assertEquals(5, bestCombCasino.chars().filter(x -> x == 'K' || x == '8').count());
    }

    @Test
    public void playTexasHoldemStraightA2345vsTJQKA() throws Exception {
        texasHoldemGame.setPlayerPreflop("2h3d");
        texasHoldemGame.setCasinoPreflop("KhQh");
        texasHoldemGame.setFlop("4s5dJd");
        texasHoldemGame.setTern("Ts");
        texasHoldemGame.setRiver("Ac");
        Matcher ourHandMatcher = card.matcher(texasHoldemGame.getCasinoPreflop());
        String[] ourHand = new String[2];
        int i = 0;
        while (ourHandMatcher.find()) {
            ourHand[i] = ourHandMatcher.group();
            i++;
        }
        Matcher playerHandMatcher = card.matcher(texasHoldemGame.getPlayerPreflop());
        String[] playerHand = new String[2];
        i = 0;
        while (playerHandMatcher.find()) {
            playerHand[i] = playerHandMatcher.group();
            i++;
        }
        Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
        String[] board = new String[5];
        i = 0;
        while (boardMatcher.find()) {
            board[i] = boardMatcher.group();
            i++;
        }
        board[3] = texasHoldemGame.getTern();
        board[4] = texasHoldemGame.getRiver();
        Map.Entry<String, Double> casinoResult = Chanses.evalCombinationByHandAndBoard(ourHand, board);
        Map.Entry<String, Double> playerResult = Chanses.evalCombinationByHandAndBoard(playerHand, board);
        double resultCasino = casinoResult.getValue();
        String bestCombCasino = casinoResult.getKey();
        double resultPlayer = playerResult.getValue();
        String bestCombPlayer = playerResult.getKey();
        assertArrayEquals(new String[]{"Kh", "Qh"}, ourHand);
        assertArrayEquals(new String[]{"2h", "3d"}, playerHand);
        assertArrayEquals(new String[]{"4s", "5d", "Jd", "Ts", "Ac"}, board);
        assertTrue(resultPlayer < resultCasino);
        assertEquals(5, bestCombPlayer.chars().filter(x -> x == 'A' || x == '2'||x == '3'||x == '4'||x == '5').count());
        assertEquals(5, bestCombCasino.chars().filter(x -> x == 'T' || x == 'J'|| x == 'Q'|| x == 'K'|| x == 'A').count());
    }

    @Test
    public void playTexasHoldemStraightTJQKAvs9TJQK() throws Exception {
        texasHoldemGame.setPlayerPreflop("2hAd");
        texasHoldemGame.setCasinoPreflop("8h9h");
        texasHoldemGame.setFlop("TsKdJd");
        texasHoldemGame.setTern("7s");
        texasHoldemGame.setRiver("Qc");
        Matcher ourHandMatcher = card.matcher(texasHoldemGame.getCasinoPreflop());
        String[] ourHand = new String[2];
        int i = 0;
        while (ourHandMatcher.find()) {
            ourHand[i] = ourHandMatcher.group();
            i++;
        }
        Matcher playerHandMatcher = card.matcher(texasHoldemGame.getPlayerPreflop());
        String[] playerHand = new String[2];
        i = 0;
        while (playerHandMatcher.find()) {
            playerHand[i] = playerHandMatcher.group();
            i++;
        }
        Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
        String[] board = new String[5];
        i = 0;
        while (boardMatcher.find()) {
            board[i] = boardMatcher.group();
            i++;
        }
        board[3] = texasHoldemGame.getTern();
        board[4] = texasHoldemGame.getRiver();
        Map.Entry<String, Double> casinoResult = Chanses.evalCombinationByHandAndBoard(ourHand, board);
        Map.Entry<String, Double> playerResult = Chanses.evalCombinationByHandAndBoard(playerHand, board);
        double resultCasino = casinoResult.getValue();
        String bestCombCasino = casinoResult.getKey();
        double resultPlayer = playerResult.getValue();
        String bestCombPlayer = playerResult.getKey();
        assertArrayEquals(new String[]{"8h", "9h"}, ourHand);
        assertArrayEquals(new String[]{"2h", "Ad"}, playerHand);
        assertArrayEquals(new String[]{"Ts", "Kd", "Jd", "7s", "Qc"}, board);
        assertTrue(resultPlayer > resultCasino);
        assertEquals(5, bestCombPlayer.chars().filter(x -> x == 'A' || x == 'K'||x == 'Q'||x == 'J'||x == 'T').count());
        assertEquals(5, bestCombCasino.chars().filter(x -> x == 'T' || x == 'J'|| x == 'Q'|| x == 'K'|| x == '9').count());
    }
}
