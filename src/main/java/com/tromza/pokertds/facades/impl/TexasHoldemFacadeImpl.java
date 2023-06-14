package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.mapper.TexasHoldemWithBetMapper;
import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.facades.TexasHoldemFacade;
import com.tromza.pokertds.mapper.BetMapper;
import com.tromza.pokertds.mapper.TexasHoldemMapper;
import com.tromza.pokertds.model.enums.Winner;
import com.tromza.pokertds.model.pairs.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;
import com.tromza.pokertds.service.TexasHoldemService;
import com.tromza.pokertds.service.UserService;
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

    public TexasHoldemFacadeImpl(UserService userService, TexasHoldemService texasHoldemService, TexasHoldemMapper texasHoldemMapper, TexasHoldemWithBetMapper texasHoldemWithBetMapper, BetMapper betMapper) {
        this.userService = userService;
        this.texasHoldemService = texasHoldemService;
        this.texasHoldemMapper = texasHoldemMapper;
        this.texasHoldemWithBetMapper = texasHoldemWithBetMapper;
        this.betMapper = betMapper;
    }

    public TexasHoldemResponse createTexasHoldem(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        TexasHoldemGame texasHoldemGame = texasHoldemService.createTexasHoldemGameForUser(user);
        return texasHoldemMapper.fromTexasHoldemToTexasHoldemResponse(texasHoldemGame);
    }

    @Override
    public TexasHoldemWihtBetResponse playingGame(Principal principal, BetPokerRequest betPokerRequest) throws InterruptedException {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        BetPoker bet = betMapper.fromBetPokerRequestToBet(betPokerRequest);
        TexasHoldemGame texasHoldemGame = texasHoldemService.playingTexasHoldem(bet, user);
        BetPoker updBet = texasHoldemService.findLastBetPokerByGameId(texasHoldemGame.getGameId()).orElseThrow(() -> new NoSuchElementException("Bet for game " + texasHoldemGame.getGameId() + " not found!"));
        TexasHoldemGameWithBetPoker texasHoldemGameWithBetPoker = new TexasHoldemGameWithBetPoker(texasHoldemGame, updBet);
        if (texasHoldemGame.getWinner() != null) {
            texasHoldemGameWithBetPoker.setCasinoCards(texasHoldemGame.getCasinoPreflop());
            if (updBet.getRound() == 4) {
                if (texasHoldemGame.getWinner().equals(Winner.PLAYER)){
                    texasHoldemGameWithBetPoker.setWinCombination(texasHoldemService.winCombination(texasHoldemGame.getPlayerPreflop(), texasHoldemGame.getFlop()+texasHoldemGame.getTern()+texasHoldemGame.getRiver()));
                }
if ((texasHoldemGame.getWinner().equals(Winner.CASINO))){
                    texasHoldemGameWithBetPoker.setWinCombination(texasHoldemService.winCombination(texasHoldemGame.getCasinoPreflop(), texasHoldemGame.getFlop()+texasHoldemGame.getTern()+texasHoldemGame.getRiver()));
                }
            }
        }
        return texasHoldemWithBetMapper.fromTexasHoldemGameWithBetPokerToResponse(texasHoldemGameWithBetPoker);
    }
}
