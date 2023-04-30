package com.tromza.pokertds.pokerLogic;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FiveCardDraw {
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
        Function<String, Integer> ranks = x -> {
            switch (x) {
                case "T":
                    return 10;
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                case "A":
                    return 14;
                default:
                    return Integer.parseInt(x);
            }
        };
        return ranks.apply(s);
    }

    public static int cardToPointsA(String s) {
        Function<String, Integer> ranks = x -> {
            switch (x) {
                case "T":
                    return 10;
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                case "A":
                    return 1;
                default:
                    return Integer.parseInt(x);
            }
        };
        return ranks.apply(s);
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        return map.entrySet().stream()
                .filter(entry -> value.equals(entry.getValue()))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    static boolean isStraigtFlush(String cardsAll, char suit) {
        boolean a = false;
        Pattern patternSuit = Pattern.compile("[2-9TJQKA]" + suit);
        Matcher matcherSuit = patternSuit.matcher(cardsAll);
        List<Integer> cards = new ArrayList<>();
        while (matcherSuit.find()) {
            cards.add(cardToPoints(matcherSuit.group().replaceAll(String.valueOf(suit), "")));
        }
        List<Integer> cardsA = new ArrayList<>();
        while (matcherSuit.find()) {
            cardsA.add(cardToPointsA(matcherSuit.group().replaceAll(String.valueOf(suit), "")));
        }
        Integer[] isStraight = cards.stream().distinct().sorted().toArray(Integer[]::new);
        Integer[] isStraightA = cardsA.stream().distinct().sorted().toArray(Integer[]::new);
        if (isStraight.length >= 5) {
            for (int j = 0; j <= (isStraight.length - 5); j++) {
                if ((isStraight[j + 4] - isStraight[j]) == 4) {
                    a = true;
                    break;
                }
            }
        }
        if (isStraightA.length >= 5) {
            for (int j = 0; j <= (isStraightA.length - 5); j++) {
                if ((isStraightA[j + 4] - isStraightA[j]) == 4) {
                    a = true;
                    break;
                }
            }
        }
        return a;
    }

    static double resultFlushOrStraightFlush(String s) {
        double res = 0;
        char[] suits = {'h', 'd', 'c', 's'};
        for (char i : suits) {
            int quantOfSuits = (int) s.chars().filter(n -> (n == i)).count();// Flush
            if (quantOfSuits >= 5) {
                if (isStraigtFlush(s, i)) {
                    res = 7.0 + RankOfStraigtFlush(s, i);
                } else {
                    res = 4.0 + RankCardOfFlush(s, i);
                }
            }
        }
        return res;
    }

    static double RankOfStraigtFlush(String cardsAll, char suit) {
        Pattern patternSuit = Pattern.compile("[2-9TJQKA]" + suit);
        Matcher matcherSuit = patternSuit.matcher(cardsAll);
        List<Integer> cards = new ArrayList<>();
        while (matcherSuit.find()) {
            cards.add(cardToPoints(matcherSuit.group().replaceAll(String.valueOf(suit), "")));
        }
        List<Integer> cardsA = new ArrayList<>();
        while (matcherSuit.find()) {
            cardsA.add(cardToPointsA(matcherSuit.group().replaceAll(String.valueOf(suit), "")));
        }
        double rank = 0.0;
        Integer[] isStraight = cards.stream().distinct().sorted().toArray(Integer[]::new);
        Integer[] isStraightA = cardsA.stream().distinct().sorted().toArray(Integer[]::new);
        if (isStraightA.length >= 5) {
            for (int j = 0; j <= (isStraightA.length - 5); j++) {
                if ((isStraightA[j + 4] - isStraightA[j]) == 4) {
                    rank = isStraightA[j + 4] * 0.01;
                }
            }
        }
        if (isStraight.length >= 5) {
            for (int j = 0; j <= (isStraight.length - 5); j++) {
                if ((isStraight[j + 4] - isStraight[j]) == 4) {
                    rank = isStraight[j + 4] * 0.01;
                }
            }
        }
        return rank;
    }

    static double RankCardOfFlush(String comb, char suit) {
        Pattern patternSuit = Pattern.compile("[2-9TJQKA]" + suit);
        Matcher matcherSuit = patternSuit.matcher(comb);
        //List<Integer> cards = new ArrayList<>();
        Integer[] ranks = Arrays.stream(comb.split(String.valueOf(suit))).map(FiveCardDraw::cardToPoints).sorted().toArray(Integer[]::new);
        /*while (matcherSuit.find()) {
            cards.add(cardToPoints(matcherSuit.group().replaceAll(String.valueOf(suit), "")));
        }*/
        //if (!cards.isEmpty()) {
         //   Integer[] ranks = cards.stream().sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
            double sum = 0;
            for (int i = 1; i <= 5; i++) {
                sum = sum + ranks[i - 1] * Math.pow(0.01, i);
            }
            return sum;
        } //else {
          //  return 0;
       // }
   // }

    static double resultStraight(String s) {
        double res = 0;
        Integer[] cardsSuitOf = Arrays.stream(s.split("[hdcs]")).map(FiveCardDraw::cardToPoints).sorted().distinct().toArray(Integer[]::new);
        Integer[] cardsSuitOfA = Arrays.stream(s.split("[hdcs]")).map(FiveCardDraw::cardToPointsA).sorted().distinct().toArray(Integer[]::new);
        if (cardsSuitOf.length >= 5) {
            for (int j = 0; j <= (cardsSuitOf.length - 5); j++) {
                if ((cardsSuitOfA[j + 4] - cardsSuitOfA[j]) == 4) {
                    res = 3.0 + cardsSuitOfA[j + 4] * 0.01;
                }
                if ((cardsSuitOf[j + 4] - cardsSuitOf[j]) == 4) {
                    res = 3.0 + cardsSuitOf[j + 4] * 0.01;
                }
            }
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
}

