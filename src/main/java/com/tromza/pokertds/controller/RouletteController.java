package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.response.RouletteWithBet;
import com.tromza.pokertds.service.RouletteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Roulette", description = "The Roulette API")
@RestController
@RequestMapping("/games/roulette")
public class RouletteController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RouletteService rouletteService;

    @Autowired
    public RouletteController(RouletteService rouletteService) {
        this.rouletteService = rouletteService;
    }

    @Operation(summary = "Create new roulette game for current user")
    @PostMapping
    public ResponseEntity<RouletteGame> createRoulette(Principal principal) {
        Optional<RouletteGame> rouletteGame = rouletteService.createRouletteGameForUser(principal);
        return rouletteGame.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "Adding a bet for current roulette game. Return info about result")
    @PutMapping
    public ResponseEntity<?> playingGame(Principal principal, @RequestBody @Valid BetRoulette bet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o.getDefaultMessage());
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            RouletteWithBet updRouletteWithBet = rouletteService.playingRoulette(bet, principal);
            return new ResponseEntity<>(updRouletteWithBet, HttpStatus.ACCEPTED);
        }
    }

    @Operation(summary = "Finish current roulette game (leaving the table)")
    @PutMapping("/finish/{id}")
    public ResponseEntity<RouletteGame> finishRouletteGameById(@PathVariable int id, Principal principal) {
        return new ResponseEntity<>(rouletteService.finishRouletteGameById(id, principal), HttpStatus.OK);
    }
}
