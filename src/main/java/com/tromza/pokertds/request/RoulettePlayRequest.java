package com.tromza.pokertds.request;

public class RoulettePlayRequest {
    int userId;
    int gameId;
    double betAmount;
    int choiceNumber;
    int typeBet;

    public int getTypeBet() {
        return typeBet;
    }

    public void setTypeBet(int typeBet) {
        this.typeBet = typeBet;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }
}
