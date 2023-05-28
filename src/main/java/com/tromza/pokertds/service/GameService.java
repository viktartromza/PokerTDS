package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.enums.GameStatus;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.repository.TexasHoldemRepository;
import com.tromza.pokertds.response.GameInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserService userService;
    private final RouletteRepository rouletteRepository;
    private final TexasHoldemRepository texasHoldemRepository;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService, RouletteRepository rouletteRepository, TexasHoldemRepository texasHoldemRepository) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.rouletteRepository = rouletteRepository;
        this.texasHoldemRepository = texasHoldemRepository;
    }

    public Game createGame(Game game) {
        game.setCreate(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.IN_PROCESS);
        return gameRepository.save(game);
    }

    public Optional<Game> getGameById(int id) {
        return gameRepository.getGameById(id);
    }

    public GameInfoResponse getGameInfoById(int id) {
        Game game = getGameById(id).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        return switch (game.getType()) {
            case ROULETTE_EU ->
                    new GameInfoResponse(rouletteRepository.findRouletteGameByGameId(id).orElseThrow(() -> new NoSuchElementException("Roulette-game not found!")));
            case TEXAS_HOLDEM ->
                    new GameInfoResponse(texasHoldemRepository.findTexasHoldemGameByGameId(id).orElseThrow(() -> new NoSuchElementException("TexasHoldem-game not found!")));
            default -> throw new NoSuchElementException("Game not found!");
        };
    }

    public void finishGame(Game game) {
        game.setFinish(new Timestamp(System.currentTimeMillis()));
        game.setStatus(GameStatus.COMPLETED);
        gameRepository.saveAndFlush(game);
    }

    public List<Game> getGamesForSingleUserById(int id) {
        return gameRepository.getGamesForSingleUser(id);
    }

    public List<Game> getGamesForSingleUser(Principal principal) {
        return gameRepository.getGamesForSingleUser(userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!")).getId());
    }

    public Optional<Game> findRouletteGameInProcess(int userId) {
        return gameRepository.getRouletteGameInProcess(userId);
    }

    public Optional<Game> findTexasHoldemGameInProcess(int userId) {
        return gameRepository.getTexasHoldemGameInProcess(userId);
    }
}
