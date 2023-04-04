package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.Game;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/game")
public class GameController {
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createGame(@RequestBody Game game) {

        gameService.createGame(game);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<ArrayList<Game>> getGamesForSingleUser(@RequestBody User user) throws ParseException {
        Optional<ArrayList<Game>> games = gameService.getGamesForSingleUser(user);
        return games.map(value->new ResponseEntity<> (value, HttpStatus.ALREADY_REPORTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
