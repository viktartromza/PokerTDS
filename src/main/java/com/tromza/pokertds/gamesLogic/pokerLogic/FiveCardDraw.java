package com.tromza.pokertds.gamesLogic.pokerLogic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class implements methods
 * that evaluate the strength of a poker hand.
 */
public class FiveCardDraw {

    /**
     * @param combination is a 10 character string where each 2 characters encode a card
     * @return power of a poker hand on a scale from 0.00 to 7.99
     */
    public static double powerOfCards(String combination) {
        double power = 0;
        List<Integer> ranges = Arrays.stream(combination.split("[hdcs]")).map(FiveCardDraw::cardToPoints).sorted().toList();
        List<Integer> rangesDistinct = ranges.stream().distinct().toList();
        int countOfRanges = rangesDistinct.size();
        if (countOfRanges == 5) { // only High Card
            if (isStraight(combination)) {
                if (isFlush(combination)) {
                    power = 4.0 + resultStraight(combination);
                } else {
                    power = resultStraight(combination);
                }
            } else {
                if (isFlush(combination)) {
                    power = 4.0 + resultHandCards(combination);
                } else {
                    power = resultHandCards(combination);
                }
            }
        } else if (countOfRanges == 4) {// One Pair
            for (Integer range : rangesDistinct) {
                int countOfMatches = (int) ranges.stream().filter(x -> x == range).count();
                if (countOfMatches == 2) {
                    power = 0.5 + range * 0.01 + resultRangesCards(ranges.stream().filter(x -> x != range).toList());
                }
                break;
            }
        } else if (countOfRanges == 2) {// Four Of A Kind or Full House
            for (Integer range : rangesDistinct) {
                int countOfMatches = (int) ranges.stream().filter(x -> x == range).count();
                if (countOfMatches == 4) {// Four Of A Kind
                    power = 6.0 + range * 0.01 + ranges.stream().filter(x -> x != range).findFirst().orElse(0) * 0.0001;
                    break;
                }
                if (countOfMatches == 3) {// Full House
                    power = 5.0 + range * 0.01 + ranges.stream().filter(x -> x != range).findFirst().orElse(0) * 0.0001;
                    break;
                }
            }
        } else { // Three Of A Kind or Two Pair
            power = 1.0;
            int firstPairRange = 0;
            for (Integer range : rangesDistinct) {
                int countOfMatches = (int) ranges.stream().filter(x -> Objects.equals(x, range)).count();
                if (countOfMatches == 3) {// Three Of A Kind
                    power = 2.0 + range * 0.01 + resultRangesCards(ranges.stream().filter(x -> !Objects.equals(x, range)).toList());
                    break;
                }
                if (countOfMatches == 2) {// Two Pair
                    if (power == 1.0) {
                        firstPairRange = range;
                        power = power + range * 0.0001;
                    } else {
                        int finalFirstPairRange = firstPairRange;
                        power = power + range * 0.01 + ranges.stream().filter(x -> (!Objects.equals(x, range) && x != finalFirstPairRange)).findFirst().orElse(0) * 0.000001;
                    }
                }
            }
        }
        return power;
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

    static boolean isStraight(String cards) {
        boolean a = false;
        List<Integer> ranks = Arrays.stream(cards.split("[hdcs]")).map(FiveCardDraw::cardToPoints).distinct().sorted().toList();
        List<Integer> ranksA = Arrays.stream(cards.split("[hdcs]")).map(FiveCardDraw::cardToPointsA).distinct().sorted().toList();
        if (ranks.size() == 5 && (ranks.stream().max(Integer::compareTo).get() - ranks.stream().min(Integer::compareTo).get()) == 4) {
            a = true;
        }
        if (ranksA.size() == 5 && (ranksA.stream().max(Integer::compareTo).get() - ranksA.stream().min(Integer::compareTo).get()) == 4) {
            a = true;
        }
        return a;
    }

    static boolean isFlush(String cards) {
        boolean a = false;
        char[] suits = {'h', 'd', 'c', 's'};
        for (char i : suits) {
            int quantOfSuits = (int) cards.chars().filter(n -> (n == i)).count();// Flush
            if (quantOfSuits == 5) {
                a = true;
            }
        }
        return a;
    }

    static double resultStraight(String cards) {
        double res;
        if (cards.contains("A") && cards.contains("2")) {
            res = 3.0 + resultHandCardsA(cards);
        } else {
            res = 3.0 + resultHandCards(cards);
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

    static double resultRangesCards(List<Integer> ranges) {
        double res = 0;
        Integer[] sortedRanges = ranges.stream().sorted().toArray(Integer[]::new);
        int count = 2;
        for (int i = sortedRanges.length - 1; i >= 0; i--) {
            res += sortedRanges[i] * Math.pow(0.01, count);
            count++;
        }
        return res;
    }
}

