package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.response.ResponseGameInfo;
import com.tromza.pokertds.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@Tag(name="Game", description="The Game API")
@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Return list of games belong to user with current id")
    @GetMapping("/")
    public ResponseEntity<ArrayList<Game>> getGamesFromOtherUser(@RequestParam(value = "userId") int id) {
        ArrayList<Game> games = gameService.getGamesForSingleUserById(id);
        return new ResponseEntity<>(games, HttpStatus.ALREADY_REPORTED);
    }

    @Operation(summary = "Return list of games belong to current user")
    @GetMapping("/info")
    public ResponseEntity<ArrayList<Game>> getGamesForUser(Principal principal) {
        ArrayList<Game> games = gameService.getGamesForSingleUser(principal);
        return new ResponseEntity<>(games, HttpStatus.ALREADY_REPORTED);
    }

    @Operation(summary = "Return info about game with current id")
    @GetMapping("/info/{id}")
    public ResponseEntity<ResponseGameInfo> getGameInfo(@PathVariable int id) {
        return new ResponseEntity<>(gameService.getGameInfoById(id), HttpStatus.ALREADY_REPORTED);
    }
}
