package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.response.RouletteWithBet;
import com.tromza.pokertds.service.RouletteService;
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

@RestController
@RequestMapping("/game/roulette")
public class RouletteController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final RouletteService rouletteService;

    @Autowired
    public RouletteController(RouletteService rouletteService) {
        this.rouletteService = rouletteService;
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
    public ResponseEntity playingGame(Principal principal, @RequestBody @Valid BetRoulette bet, BindingResult bindingResult) {
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

    @PutMapping("/finish/{id}")
    public ResponseEntity<RouletteGame> finishRouletteGameById(@PathVariable int id, Principal principal) {
        return new ResponseEntity<>(rouletteService.finishRouletteGameById(id, principal), HttpStatus.CREATED);
    }
}
