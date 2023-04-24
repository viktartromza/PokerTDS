package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.response.ResponseGameInfo;
import com.tromza.pokertds.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;


@RestController
@RequestMapping("/games")
public class GameController {
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public ResponseEntity<ArrayList<Game>> getGamesForAnotherUser(@RequestParam(value = "userId") int id) {
        ArrayList<Game> games = gameService.getGamesForSingleUserById(id);
        return new ResponseEntity<>(games, HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/info")
    public ResponseEntity<ArrayList<Game>> getGamesForUser(Principal principal) {
        ArrayList<Game> games = gameService.getGamesForSingleUser(principal);
        return new ResponseEntity<>(games, HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseGameInfo> getGameInfo(@PathVariable int id) {
        return new ResponseEntity<>(gameService.getGameInfoById(id), HttpStatus.ALREADY_REPORTED);
    }
}
