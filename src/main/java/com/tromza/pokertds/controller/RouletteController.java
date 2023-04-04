package com.tromza.pokertds.controller;

import com.tromza.pokertds.GameLogic.Roulette;
import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.request.RoulettePlayRequest;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import org.hibernate.annotations.Parameter;
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



    @PostMapping("/")
    public ResponseEntity<RouletteGame> createRoulette(@RequestBody User user) {
        Game game = new Game();
        game.setType(GameType.ROULETTE_EU);
        game = gameService.createGame(game);
        RouletteGame rouletteGame = new RouletteGame();
        rouletteGame.setGameId(game.getId());

        //userService.addGameToUser;TODO вносить запись в линковочную базу
        return new ResponseEntity<>(rouletteService.createRouletteGame(rouletteGame), HttpStatus.CREATED);
    }

    @PostMapping("/play")
    public ResponseEntity<RouletteGame> playingGame(@RequestBody BetRoulette bet) {
        RouletteGame rouletteGame = rouletteService.getRouletteGameById(bet.getGameId()).orElseGet(null);
        rouletteService.saveBetRoulette(bet);
//TODO

        return new ResponseEntity<>(rouletteGame, HttpStatus.CREATED);

    }
}
