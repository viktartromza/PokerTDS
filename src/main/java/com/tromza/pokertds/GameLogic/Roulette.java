package com.tromza.pokertds.GameLogic;


import java.util.Random;

public class Roulette  {
    int win = 0;
    int lose = 0;
    int spin = 0;


    /**
     * 0 - Even, 1 - Odd, 2 - Number
     */
    int typeBet;

    public void setTypeBet(int typeBet) {
        this.typeBet = typeBet;
    }

    public double gameResultNumber(double betAmount, int choiceNumber) {
        int result = 0;
        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        spin++;

        if (rouletteNumber == choiceNumber) {
            result = 36;
            win++;
        } else {
            result = 0;
            lose--;
        }

        return betAmount * (result - 1);
    }

    public double gameResultEvenOdd(double betAmount) {
        int result = 0;
        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        spin++;
        int typeBet = this.typeBet;
         if (rouletteNumber == 0 || rouletteNumber % 2 != typeBet) {
            result = 0;
            lose--;
        } else {
            result = 1;
            win++;
        }
        return betAmount * (result - 1);
    }
}
