package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.facades.RouletteFacade;
import com.tromza.pokertds.request.BetRouletteRequest;
import com.tromza.pokertds.response.RouletteResponse;
import com.tromza.pokertds.response.RouletteWithBet;
import com.tromza.pokertds.service.impl.RouletteServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final RouletteFacade rouletteFacade;

    @Autowired
    public RouletteController(RouletteFacade rouletteFacade) {
        this.rouletteFacade = rouletteFacade;
    }

    @Operation(summary = "Create new roulette game for current user")
    @PostMapping
    public ResponseEntity<RouletteResponse> createRoulette(Principal principal) {
        RouletteResponse rouletteGame = rouletteFacade.createRoulette(principal);
        return new ResponseEntity<>(rouletteGame, HttpStatus.CREATED);
    }

    @Operation(summary = "Adding a bet for current roulette game. Return info about result")
    @ApiResponse(content = @Content(schema = @Schema(implementation = RouletteWithBet.class)))
    @PutMapping
    public ResponseEntity<?> playingGame(Principal principal, @RequestBody @Valid BetRouletteRequest bet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o.getDefaultMessage());
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            RouletteWithBet updRouletteWithBet = rouletteFacade.playingGame(principal, bet);
            return new ResponseEntity<>(updRouletteWithBet, HttpStatus.ACCEPTED);
        }
    }

    @Operation(summary = "Finish current roulette game (leaving the table)")
    @PutMapping("/finish/{id}")
    public ResponseEntity<RouletteResponse> finishRouletteGameById(@PathVariable int id, Principal principal) {
        return new ResponseEntity<>(rouletteFacade.finishRouletteGameById(id, principal), HttpStatus.OK);
    }
}
