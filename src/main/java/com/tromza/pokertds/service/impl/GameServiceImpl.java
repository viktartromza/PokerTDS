package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.enums.GameStatus;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.service.GameService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;


    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Game game) {
        game.setCreate(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.IN_PROCESS);
        return gameRepository.save(game);
    }

    public Optional<Game> getGameById(int id) {
        return gameRepository.getGameById(id);
    }

    public void finishGame(Game game) {
        game.setFinish(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.COMPLETED);
        gameRepository.saveAndFlush(game);
    }

    public List<Game> getGamesForSingleUserById(int id) {
        return gameRepository.getGamesForSingleUser(id);
    }

    public List<Game> getGamesForSingleUser(User user) {
        return gameRepository.getGamesForSingleUser(user.getId());
    }

    public Optional<Game> findRouletteGameInProcess(int userId) {
        return gameRepository.getRouletteGameInProcess(userId);
    }

    public Optional<Game> findTexasHoldemGameInProcess(int userId) {
        return gameRepository.getTexasHoldemGameInProcess(userId);
    }
}
