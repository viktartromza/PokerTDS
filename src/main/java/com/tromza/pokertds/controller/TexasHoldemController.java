package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.TexasHoldemFacade;
import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;
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


@Tag(name = "TexasHoldem", description = "The TexasHoldem API")
@RestController
@RequestMapping("/games/poker/texas")
public class TexasHoldemController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TexasHoldemFacade texasHoldemFacade;

    @Autowired
    public TexasHoldemController(TexasHoldemFacade texasHoldemFacade) {
        this.texasHoldemFacade = texasHoldemFacade;
    }

    @Operation(summary = "Create new texas hold'em for current user")
    @PostMapping
    public ResponseEntity<TexasHoldemResponse> createTexasHoldem(Principal principal) {
        TexasHoldemResponse texasHoldemGame = texasHoldemFacade.createTexasHoldem(principal);
        return new ResponseEntity<>(texasHoldemGame, HttpStatus.CREATED);
    }

    @Operation(summary = "Adding a bet for current texas hold'em. Return info about current state and casino decision")
    @ApiResponse(content=@Content(schema = @Schema(implementation = TexasHoldemWihtBetResponse.class)))
    @PutMapping
    public ResponseEntity<?> playingGame(Principal principal, @RequestBody @Valid BetPokerRequest bet, BindingResult bindingResult) throws InterruptedException {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o.getDefaultMessage());
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            TexasHoldemWihtBetResponse texasHoldemWihtBetResponse = texasHoldemFacade.playingGame(principal, bet);
            return new ResponseEntity<>(texasHoldemWihtBetResponse, HttpStatus.ACCEPTED);
        }
    }
}
