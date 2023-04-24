package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.GameStatus;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.response.ResponseGameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserService userService;
    private final RouletteRepository rouletteRepository;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService, RouletteRepository rouletteRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.rouletteRepository = rouletteRepository;
    }

    public Game createGame(Game game) {
        game.setCreate(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.IN_PROCESS);
        return gameRepository.save(game);
    }

    public Optional<Game> getGameById(int id) {
        return gameRepository.getGameById(id);
    }

    public ResponseGameInfo getGameInfoById(int id) {
        Game game = getGameById(id).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        switch (game.getType()) {
            case ROULETTE_EU:
                return new ResponseGameInfo(rouletteRepository.findRouletteGameByGameId(id).orElseThrow(() -> new NoSuchElementException("Roulette-game not found!")));
            default:
                throw new NoSuchElementException("Game not found!");
        }
    }

    public Game finishGame(Game game) {
        game.setFinish(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.COMPLETED);
        return gameRepository.saveAndFlush(game);
    }

    public ArrayList<Game> getGamesForSingleUserById(int id) {
        return gameRepository.getGamesForSingleUser(id);
    }

    public ArrayList<Game> getGamesForSingleUser(Principal principal) {
        return gameRepository.getGamesForSingleUser(userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!")).getId());
    }

    public Optional<Game> findRouletteGameInProcess(int userId) {
        return gameRepository.getRouletteGameInProcess(userId);
    }

}
