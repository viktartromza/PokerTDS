package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;

import java.security.Principal;

public interface TexasHoldemFacade {

    TexasHoldemResponse createTexasHoldem(Principal principal);

    TexasHoldemWihtBetResponse playingGame(Principal principal, BetPokerRequest betPokerRequest) throws InterruptedException;
}
