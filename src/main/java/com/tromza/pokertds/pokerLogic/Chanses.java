package com.tromza.pokertds.pokerLogic;

import java.util.ArrayList;


public class Chanses {
    static final int QUANTITYALLCOMBINATIONS = 2598960;
    static final double AVERAGEPOWER = 0.43505781564375623;

    static int[] genCombinations(int[] arr, int k, int n) {//3 из 5, k=3, n=5
        if (arr == null) {
            arr = new int[k];
            for (int i = 0; i < k; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = k - 1; i >= 0; i--) {
            if (arr[i] < n - k + i + 1) {
                arr[i]++;
                for (int j = i; j < k - 1; j++) {
                    arr[j + 1] = arr[j] + 1;
                }
                return arr;
            }
        }
        return null;
    }

    public static double compCombinations(String[] hand, String[] board) {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        thisDeck.remove(hand[0]);
        thisDeck.remove(hand[1]);
        if (board == null) {
            String[] deckArr = new String[thisDeck.size()];
            thisDeck.toArray(deckArr);
            String combination;
            int[] arr = null;
            ArrayList<Double> handValue = new ArrayList<>();
            while ((arr = genCombinations(arr, 3, thisDeck.size())) != null) {
                combination = deckArr[(arr[0] - 1)] + deckArr[(arr[1] - 1)] + deckArr[(arr[2] - 1)] + hand[0] + hand[1];
                //System.out.println(combination +" result: " + FiveCardDraw.process(combination));
                handValue.add(FiveCardDraw.process(combination));
            }
            return handValue.stream().mapToDouble(x -> x).average().getAsDouble();
        } else {
            String[] handWithBoard = new String[board.length + 2];
            System.arraycopy(hand, 0, handWithBoard, 0, 2);
            for (int i = 0; i < board.length; i++) {
                handWithBoard[i + 2] = board[i];
                thisDeck.remove(board[i]);
            }
            String[] deckArr = new String[thisDeck.size()];
            thisDeck.toArray(deckArr);
            double aktualValue = evalCombination(handWithBoard);
            String[] futureHandWithTable = new String[7];
            System.arraycopy(handWithBoard, 0, futureHandWithTable, 0, handWithBoard.length);

            int[] arr = null;
            ArrayList<Double> handValue = new ArrayList<>();
            while ((arr = genCombinations(arr, 7 - handWithBoard.length, thisDeck.size())) != null) {
                if (handWithBoard.length == 5) {
                    futureHandWithTable[5] = deckArr[(arr[0] - 1)];
                    futureHandWithTable[6] = deckArr[(arr[1] - 1)];
                } else if (handWithBoard.length == 6) {
                    futureHandWithTable[6] = deckArr[(arr[0] - 1)];
                }
                //System.out.println(combination +" result: " + FiveCardDraw.process(combination));
                handValue.add(evalCombination(futureHandWithTable));
            }
            long count = handValue.size();
            long up = handValue.stream().mapToDouble(x -> x).filter(x -> x > aktualValue).count();
            double max = handValue.stream().mapToDouble(x -> x).filter(x -> x > aktualValue).average().getAsDouble();
            return max * up / count;
        }
    }

    public static double evalCombination(String[] handWithBoard) {
        String combination;
        int[] arr = null;
        ArrayList<Double> handValue = new ArrayList<>();
        while ((arr = genCombinations(arr, 5, handWithBoard.length)) != null) {
            combination = handWithBoard[(arr[0] - 1)] + handWithBoard[(arr[1] - 1)] + handWithBoard[(arr[2] - 1)] + handWithBoard[(arr[3] - 1)] + handWithBoard[(arr[4] - 1)];
            //System.out.println(combination +" result: " + FiveCardDraw.process(combination));
            handValue.add(FiveCardDraw.process(combination));
        }
        return handValue.stream().mapToDouble(x -> x).max().getAsDouble();
    }

    public static double evalCombinationByHandAndBoard(String[] hand, String[] board) {
        String[] handWithBoard = new String[hand.length + board.length];
        System.arraycopy(hand, 0, handWithBoard, 0, 2);
        System.arraycopy(board, 0, handWithBoard, 2, board.length);
        String combination;
        int[] arr = null;
        ArrayList<Double> handValue = new ArrayList<>();
        while ((arr = genCombinations(arr, 5, handWithBoard.length)) != null) {
            combination = handWithBoard[(arr[0] - 1)] + handWithBoard[(arr[1] - 1)] + handWithBoard[(arr[2] - 1)] + handWithBoard[(arr[3] - 1)] + handWithBoard[(arr[4] - 1)];
            //System.out.println(combination + " result: " + FiveCardDraw.process(combination));
            handValue.add(FiveCardDraw.process(combination));
        }
        return handValue.stream().mapToDouble(x -> x).max().getAsDouble();
    }

    public static double compCombinationsPlayer(String[] hand, String[] board) {
        Deck deck = new Deck();
        ArrayList<String> thisDeck = deck.get();
        thisDeck.remove(hand[0]);
        thisDeck.remove(hand[1]);
        if (board == null) {
            String[] deckArr = new String[thisDeck.size()];
            thisDeck.toArray(deckArr);
            String combination;
            int[] arr = null;
            ArrayList<Double> handValue = new ArrayList<>();
            while ((arr = genCombinations(arr, 3, thisDeck.size())) != null) {
                combination = deckArr[(arr[0] - 1)] + deckArr[(arr[1] - 1)] + deckArr[(arr[2] - 1)] + hand[0] + hand[1];
                //System.out.println(combination +" result: " + FiveCardDraw.process(combination));
                handValue.add(FiveCardDraw.process(combination));
            }
            return (QUANTITYALLCOMBINATIONS * AVERAGEPOWER - handValue.stream().mapToDouble(x -> x).average().getAsDouble() * handValue.size()) / (QUANTITYALLCOMBINATIONS - handValue.size());
        } else {
            for (String s : board) {
                thisDeck.remove(s);
            }
            String[] deckArr = new String[thisDeck.size()];
            thisDeck.toArray(deckArr);
            String[] optionalHand = new String[2];
            int[] arr = null;
            ArrayList<Double> handValue = new ArrayList<>();
            while ((arr = genCombinations(arr, 2, thisDeck.size())) != null) {
                optionalHand[0] = deckArr[(arr[0] - 1)];
                optionalHand[1] = deckArr[(arr[1] - 1)];
                //System.out.println(combination +" result: " + FiveCardDraw.process(combination));
                handValue.add(evalCombinationByHandAndBoard(optionalHand, board));
            }
            return handValue.stream().mapToDouble(x -> x).average().getAsDouble();
        }
    }
}


