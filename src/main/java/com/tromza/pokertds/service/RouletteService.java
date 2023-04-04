package com.tromza.pokertds.service;

import com.tromza.pokertds.GameLogic.Roulette;
import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.BetType;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.repository.BetRepository;
import com.tromza.pokertds.repository.RouletteRepository;
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

    public RouletteGame createRouletteGame(RouletteGame rouletteGame) {
        rouletteGame.setSpin(0);
        rouletteGame.setWins(0);
        rouletteGame.setLosses(0);
        return rouletteRepository.save(rouletteGame);
    }

    public BetRoulette saveBetRoulette(BetRoulette betRoulette) {
        return betRepository.save(betRoulette);
    }

    public int[] gameResult(RouletteGame rouletteGame, BetRoulette betRoulette) {
        int[] result = new int[3];// 0 - roulette number, 1-win/lose, 2 - result
        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        result[0] = rouletteNumber;
        if (betRoulette.getType() == BetType.NUMBER) {//TODO

        }

        return result;
    }


}
