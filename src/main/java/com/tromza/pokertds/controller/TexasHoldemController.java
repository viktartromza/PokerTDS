package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.TexasHoldemGame;
import com.tromza.pokertds.response.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.service.TexasHoldemService;
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

@Tag(name = "TexasHoldem", description = "The TexasHoldem API")
@RestController
@RequestMapping("/games/poker/texas")
public class TexasHoldemController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TexasHoldemService texasHoldemService;

    @Autowired
    public TexasHoldemController(TexasHoldemService texasHoldemService) {
        this.texasHoldemService = texasHoldemService;
    }

    @Operation(summary = "Create new texas hold'em for current user")
    @PostMapping
    public ResponseEntity<TexasHoldemGame> createTexasHoldem(Principal principal) {
        Optional<TexasHoldemGame> texasHoldemGame = texasHoldemService.createTexasHoldemGameForUser(principal);
        return texasHoldemGame.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "Adding a bet for current texas hold'em. Return info about current state and casino decision")
    @PutMapping
    public ResponseEntity<?> playingGame(Principal principal, @RequestBody @Valid BetPoker bet, BindingResult bindingResult) throws InterruptedException {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o.getDefaultMessage());
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            TexasHoldemGameWithBetPoker texasHoldemGameWithBetPoker = texasHoldemService.playingTexasHoldem(bet, principal);
            return new ResponseEntity<>(texasHoldemGameWithBetPoker, HttpStatus.ACCEPTED);
        }
    }
}
