package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.mapper.TexasHoldemWithBetMapper;
import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.facades.TexasHoldemFacade;
import com.tromza.pokertds.mapper.BetMapper;
import com.tromza.pokertds.mapper.TexasHoldemMapper;
import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;
import com.tromza.pokertds.service.TexasHoldemService;
import com.tromza.pokertds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.NoSuchElementException;

@Service
public class TexasHoldemFacadeImpl implements TexasHoldemFacade {
    private final UserService userService;
    private final TexasHoldemService texasHoldemService;
    private final TexasHoldemMapper texasHoldemMapper;
    private final TexasHoldemWithBetMapper texasHoldemWithBetMapper;
    private final BetMapper betMapper;

@Autowired
    public TexasHoldemFacadeImpl(UserService userService, TexasHoldemService texasHoldemService, TexasHoldemMapper texasHoldemMapper, TexasHoldemWithBetMapper texasHoldemWithBetMapper, BetMapper betMapper) {
        this.userService = userService;
        this.texasHoldemService = texasHoldemService;
        this.texasHoldemMapper = texasHoldemMapper;
        this.texasHoldemWithBetMapper = texasHoldemWithBetMapper;
        this.betMapper = betMapper;
    }

    public TexasHoldemResponse createTexasHoldem(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(()->new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        TexasHoldemGame texasHoldemGame = texasHoldemService.createTexasHoldemGameForUser(user);
        return texasHoldemMapper.fromTexasHoldemToTexasHoldemResponse(texasHoldemGame);
    }

    @Override
    public TexasHoldemWihtBetResponse playingGame(Principal principal, BetPokerRequest betPokerRequest) throws InterruptedException {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(()->new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        BetPoker bet = betMapper.fromBetPokerRequestToBet(betPokerRequest);
        return texasHoldemWithBetMapper.fromTexasHoldemGameWithBetPokerToResponse(texasHoldemService.playingTexasHoldem(bet,user));
    }
}
