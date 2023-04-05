package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.BetType;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.repository.BetRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.request.RouletteWithBet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class RouletteService {
    BetRepository betRepository;
    RouletteRepository rouletteRepository;

    @Autowired
    public RouletteService(RouletteRepository rouletteRepository, BetRepository betRepository) {
        this.rouletteRepository = rouletteRepository;
        this.betRepository = betRepository;
    }

    public Optional<RouletteGame> getRouletteGameById(int id) {
        return rouletteRepository.findById(id);
    }

    public Optional<RouletteGame> getRouletteGameByGameId(int id) {
        return rouletteRepository.findRouletteGameByGameId(id);
    }

    public RouletteGame createRouletteGame(RouletteGame rouletteGame) {
        rouletteGame.setSpin(0);
        rouletteGame.setWins(0);
        rouletteGame.setLosses(0);
        return rouletteRepository.save(rouletteGame);
    }

    public RouletteGame updateRouletteGame(RouletteGame rouletteGame) {
        return rouletteRepository.saveAndFlush(rouletteGame);
    }

    public BetRoulette saveBetRoulette(BetRoulette betRoulette) {
        return betRepository.save(betRoulette);
    }
    public BetRoulette updateBetRoulette(BetRoulette betRoulette) {
        return betRepository.saveAndFlush(betRoulette);
    }


    public RouletteWithBet play(RouletteWithBet rouletteWithBet) {

        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        rouletteWithBet.getBetRoulette().setRouletteNumber(rouletteNumber);
        rouletteWithBet.getRouletteGame().setSpin(rouletteWithBet.getRouletteGame().getSpin()+1);
        double amount = rouletteWithBet.getBetRoulette().getAmount();
        double result = rouletteWithBet.getRouletteGame().getResult();
        int wins = rouletteWithBet.getRouletteGame().getWins();
        int losses = rouletteWithBet.getRouletteGame().getLosses();


        if (rouletteWithBet.getBetRoulette().getType() == BetType.NUMBER) {
            if (rouletteNumber == Integer.parseInt(rouletteWithBet.getBetRoulette().getPlayerChoice())) {
                rouletteWithBet.getBetRoulette().setWinAmount(36 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result+35*amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result-amount);
            }
        } else if (rouletteWithBet.getBetRoulette().getType() == BetType.EVEN) {
            if (rouletteNumber == 0 || rouletteNumber % 2 != 0) {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result-amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result+amount);
            }
        } else if (rouletteNumber % 2 == 0) {
            rouletteWithBet.getBetRoulette().setWinAmount(0.00);
            rouletteWithBet.getRouletteGame().setLosses(losses + 1);
            rouletteWithBet.getRouletteGame().setResult(result-amount);
        } else {
            rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
            rouletteWithBet.getRouletteGame().setWins(wins + 1);
            rouletteWithBet.getRouletteGame().setResult(result+amount);
        }
        return rouletteWithBet;
    }
}
