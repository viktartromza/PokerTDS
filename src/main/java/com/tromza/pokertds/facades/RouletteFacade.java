package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.request.BetRouletteRequest;
import com.tromza.pokertds.model.response.RouletteResponse;
import com.tromza.pokertds.model.response.RouletteWithBetResponse;

import java.security.Principal;

public interface RouletteFacade {
    RouletteResponse createRoulette(Principal principal);

    RouletteWithBetResponse playingGame(Principal principal, BetRouletteRequest betRouletteRequest);

    RouletteResponse finishRouletteGameById (int id, Principal principal);
}
