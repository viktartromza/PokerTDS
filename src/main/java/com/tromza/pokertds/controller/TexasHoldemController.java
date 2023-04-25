package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.TexasHoldemGame;
import com.tromza.pokertds.service.TexasHoldemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/game/poker/texas")
public class TexasHoldemController {

    private final TexasHoldemService texasHoldemService;
@Autowired
    public TexasHoldemController(TexasHoldemService texasHoldemService) {
        this.texasHoldemService = texasHoldemService;
    }

    @PostMapping("/")
    public ResponseEntity<TexasHoldemGame> createTexasHoldem(Principal principal) {
        Optional<TexasHoldemGame> texasHoldemGame = texasHoldemService.createTexasHoldemGameForUser(principal);
        return texasHoldemGame.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
