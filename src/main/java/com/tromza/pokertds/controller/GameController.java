package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.GameFacade;
import com.tromza.pokertds.model.response.GameInfoResponse;
import com.tromza.pokertds.model.response.GameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Game", description = "The Game API")
@RestController
@RequestMapping("/games")
public class GameController {
    private final GameFacade gameFacade;

    public GameController(GameFacade gameFacade) {
        this.gameFacade = gameFacade;
    }

    @Operation(summary = "Return list of games belong to user with current id")
    @GetMapping
    public ResponseEntity<List<GameResponse>> getGamesFromOtherUser(@RequestParam(value = "userId") int id) {
        List<GameResponse> games = gameFacade.getGamesByUserId(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Operation(summary = "Return list of games belong to current user")
    @GetMapping("/info")
    public ResponseEntity<List<GameResponse>> getGamesForUser(Principal principal) {
        List<GameResponse> games = gameFacade.getGamesForPrincipal(principal);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Operation(summary = "Return info about game with current id")
    @GetMapping("/info/{id}")
    public ResponseEntity<GameInfoResponse> getGameInfo(@PathVariable int id) {
        return new ResponseEntity<>(gameFacade.getGameInfoByGameId(id), HttpStatus.OK);
    }
}
