package com.tromza.pokertds.facades;

import com.tromza.pokertds.request.BetRouletteRequest;
import com.tromza.pokertds.response.RouletteResponse;
import com.tromza.pokertds.response.RouletteWithBet;

import java.security.Principal;

public interface RouletteFacade {
    RouletteResponse createRoulette(Principal principal);

    RouletteWithBet playingGame(Principal principal, BetRouletteRequest betRouletteRequest);

    RouletteResponse finishRouletteGameById (int id, Principal principal);
}
