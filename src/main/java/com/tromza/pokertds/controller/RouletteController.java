package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.request.RouletteWithBet;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game/roulette")
public class RouletteController {
    RouletteService rouletteService;
    GameService gameService;
    UserService userService;

    @Autowired
    public RouletteController(RouletteService rouletteService, GameService gameService, UserService userService) {
        this.rouletteService = rouletteService;
        this.gameService = gameService;
        this.userService = userService;
    }


    /**
     * @param user
     * @return new roulette game
     */
    @PostMapping("/")
    public ResponseEntity<RouletteGame> createRoulette(@RequestBody User user) {
        Game game = new Game();
        game.setType(GameType.ROULETTE_EU);
        game = gameService.createGame(game);
        RouletteGame rouletteGame = new RouletteGame();
        rouletteGame.setGameId(game.getId());
        userService.addGameToUser(user, game);
        return new ResponseEntity<>(rouletteService.createRouletteGame(rouletteGame), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<RouletteWithBet> playingGame(@RequestBody BetRoulette bet) {
        RouletteGame rouletteGame = rouletteService.getRouletteGameByGameId(bet.getGameId()).orElseGet(null);
        rouletteService.saveBetRoulette(bet);
        RouletteWithBet rouletteWithBet = new RouletteWithBet(rouletteGame, bet);
        RouletteWithBet updRouletteWithBet = rouletteService.play(rouletteWithBet);
        rouletteService.updateRouletteGame(updRouletteWithBet.getRouletteGame());
        rouletteService.updateBetRoulette(updRouletteWithBet.getBetRoulette());

        return new ResponseEntity<>(updRouletteWithBet, HttpStatus.ACCEPTED);

    }
}
