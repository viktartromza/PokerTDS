package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
