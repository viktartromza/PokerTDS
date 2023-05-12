package com.tromza.pokertds.pokerLogic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This class implements methods
 * that evaluate the strength of a poker hand.
 */
public class FiveCardDraw {

    /**
     * @param combination is a 10 character string where each 2 characters encode a card
     * @return power of a poker hand in scale on a scale from 0.00 to 7.99
     */
    static double process(String combination) {

        double fourOfAKind = 0, fullHouse = 0, threeOfAKind = 0, twoPairs = 0, pair = 0, handCards = 0;
        double straightFlush = resultFlushOrStraightFlush(combination);
        double straight = resultStraight(combination);
        Integer[] cardsSuitOfAll = Arrays.stream(combination.split("[hdcs]")).map(FiveCardDraw::cardToPoints).sorted().toArray(Integer[]::new);
        Map<Integer, Integer> matches = new HashMap<>();
        for (int c = 2; c <= 14; c++) {
            matches.put(c, 0);
            for (Integer integer : cardsSuitOfAll) {
                if (c == integer) {
                    matches.put(c, matches.get(c) + 1);
                }
            }
        }
        if (matches.containsValue(4)) {// Four Of A Kind
            double fourOfAKindValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 4).findAny().get();
            double lastCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).findAny().get();
            fourOfAKind = 6.0 + fourOfAKindValue * 0.01 + lastCard * 0.0001;
        } else if (matches.containsValue(3)) {// Three Of A Kind
            double threeOfAKindValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 3).findAny().get();
            if (matches.containsValue(2)) {// Full House
                double pairValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 2).findAny().get();
                fullHouse = 5.0 + threeOfAKindValue * 0.01 + pairValue * 0.0001;
            } else {
                double highCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).max(Integer::compareTo).get();
                double lastCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).min(Integer::compareTo).get();
                threeOfAKind = 2.0 + threeOfAKindValue * 0.01 + highCard * 0.0001 + lastCard * 0.000001;
            }
        } else if (matches.containsValue(2)) {// Pair
            if (matches.keySet().stream().filter(key -> matches.get(key) == 2).count() > 1) {// Two pairs
                double pairStrongValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 2).max(Integer::compareTo).get();
                double pairSecondValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 2).min(Integer::compareTo).get();
                double lastCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).findAny().get();
                twoPairs = 1.0 + pairStrongValue * 0.01 + pairSecondValue * 0.0001 + lastCard * 0.000001;
            }
            double pairValue = (double) matches.keySet().stream().filter(key -> matches.get(key) == 2).findAny().get();
            double highCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).max(Integer::compareTo).get();
            double mediumCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).max(Integer::compareTo).get();
            double lastCard = (double) matches.keySet().stream().filter(key -> matches.get(key) == 1).min(Integer::compareTo).get();
            pair = 0.5 + pairValue * 0.01 + highCard * 0.0001 + mediumCard * 0.000001 + lastCard * 0.00000001;
        } else {
            handCards = resultHandCards(combination);
        }
        return Stream.of(straightFlush, threeOfAKind, fullHouse, straight, fourOfAKind, twoPairs, pair, handCards).max(Double::compareTo).get();
    }

    public static int cardToPoints(String s) {
        Function<String, Integer> ranks = x -> switch (x) {
            case "T" -> 10;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            case "A" -> 14;
            default -> Integer.parseInt(x);
        };
        return ranks.apply(s);
    }

    public static int cardToPointsA(String s) {
        Function<String, Integer> ranks = x -> switch (x) {
            case "T" -> 10;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            case "A" -> 1;
            default -> Integer.parseInt(x);
        };
        return ranks.apply(s);
    }

    static double resultFlushOrStraightFlush(String s) {
        double res = 0;
        char[] suits = {'h', 'd', 'c', 's'};
        for (char i : suits) {
            int quantOfSuits = (int) s.chars().filter(n -> (n == i)).count();// Flush
            if (quantOfSuits == 5) {
                if (isStraightFlush(s, i)) {
                    res = 4.0 + resultStraight(s);
                } else {
                    res = 4.0 + resultHandCards(s);
                }
            }
        }
        return res;
    }

    static boolean isStraightFlush(String cards, char suit) {
        boolean a = false;
        List<Integer> ranks = Arrays.stream(cards.split(String.valueOf(suit))).map(FiveCardDraw::cardToPoints).distinct().sorted().toList();
        List<Integer> ranksA = Arrays.stream(cards.split(String.valueOf(suit))).map(FiveCardDraw::cardToPointsA).distinct().sorted().toList();
        if (ranks.size() == 5 && (ranks.stream().max(Integer::compareTo).get() - ranks.stream().min(Integer::compareTo).get()) == 4) {
            a = true;
        }
        if (ranks.size() == 5 && (ranksA.stream().max(Integer::compareTo).get() - ranksA.stream().min(Integer::compareTo).get()) == 4) {
            a = true;
        }
        return a;
    }

    static double resultStraight(String cards) {
        double res = 0;
        List<Integer> ranks = Arrays.stream(cards.split("[hdcs]")).map(FiveCardDraw::cardToPoints).distinct().sorted().toList();
        List<Integer> ranksA = Arrays.stream(cards.split("[hdcs]")).map(FiveCardDraw::cardToPointsA).distinct().sorted().toList();
        if (ranks.size() == 5 && (ranks.stream().max(Integer::compareTo).get() - ranks.stream().min(Integer::compareTo).get()) == 4) {
            res = 3.0 + resultHandCards(cards);
        }
        if (ranks.size() == 5 && (ranksA.stream().max(Integer::compareTo).get() - ranksA.stream().min(Integer::compareTo).get()) == 4) {
            res = 3.0 + resultHandCardsA(cards);
        }
        return res;
    }

    static double resultHandCards(String hand) {
        double res = 0;
        Integer[] ranks = Arrays.stream(hand.split("[hdcs]")).map(FiveCardDraw::cardToPoints).sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
        for (int i = 1; i <= 5; i++) {
            res = res + ranks[i - 1] * Math.pow(0.01, i);
        }
        return res;
    }

    static double resultHandCardsA(String hand) {
        double res = 0;
        Integer[] ranks = Arrays.stream(hand.split("[hdcs]")).map(FiveCardDraw::cardToPointsA).sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
        for (int i = 1; i <= 5; i++) {
            res = res + ranks[i - 1] * Math.pow(0.01, i);
        }
        return res;
    }
}

