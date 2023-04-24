package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.request.RouletteWithBet;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/game/roulette")
public class RouletteController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final RouletteService rouletteService;
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public RouletteController(RouletteService rouletteService, GameService gameService, UserService userService) {
        this.rouletteService = rouletteService;
        this.gameService = gameService;
        this.userService = userService;
    }

    /**
     * @return new roulette game
     */
    @PostMapping("/")
    public ResponseEntity<RouletteGame> createRoulette(Principal principal) {
        Optional<RouletteGame> rouletteGame = rouletteService.createRouletteGameForUser(principal);
        return rouletteGame.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping("/")
    public ResponseEntity<RouletteWithBet> playingGame(Principal principal, @RequestBody @Valid BetRoulette bet, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            RouletteWithBet updRouletteWithBet = rouletteService.playingRoulette(bet, principal);

            return new ResponseEntity<>(updRouletteWithBet, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<RouletteGame> finishRouletteGameById(@PathVariable int id, Principal principal) throws Exception {
        // TODO user update
        return new ResponseEntity<>(rouletteService.finishRouletteGameById(id,principal), HttpStatus.CREATED);
    }
}
