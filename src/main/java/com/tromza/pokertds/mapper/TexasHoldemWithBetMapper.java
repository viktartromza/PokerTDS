package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.pairs.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;
import org.springframework.stereotype.Component;

@Component
public class TexasHoldemWithBetMapper {
    public TexasHoldemWihtBetResponse fromTexasHoldemGameWithBetPokerToResponse(TexasHoldemGameWithBetPoker texasHoldemWithBet) {
TexasHoldemWihtBetResponse response = new TexasHoldemWihtBetResponse();
response.setTexasHoldemId(texasHoldemWithBet.getTexasHoldemGame().getId());
response.setGameId(texasHoldemWithBet.getTexasHoldemGame().getGameId());
response.setBank(texasHoldemWithBet.getTexasHoldemGame().getBank());
response.setPlayerDeposit(texasHoldemWithBet.getTexasHoldemGame().getPlayerDeposit());
response.setPlayerDeposit(texasHoldemWithBet.getTexasHoldemGame().getPlayerDeposit());
response.setPlayerPreflop(texasHoldemWithBet.getTexasHoldemGame().getPlayerPreflop());
if(texasHoldemWithBet.getTexasHoldemGame().getFlop()!=null) {
    response.setFlop(texasHoldemWithBet.getTexasHoldemGame().getFlop());
}
if(texasHoldemWithBet.getTexasHoldemGame().getTern()!=null) {
    response.setTern(texasHoldemWithBet.getTexasHoldemGame().getTern());
}
if(texasHoldemWithBet.getTexasHoldemGame().getRiver()!=null) {
    response.setRiver(texasHoldemWithBet.getTexasHoldemGame().getRiver());
}
response.setResult(texasHoldemWithBet.getTexasHoldemGame().getResult());
if(texasHoldemWithBet.getTexasHoldemGame().getWinner()!=null) {
    response.setWinner(texasHoldemWithBet.getTexasHoldemGame().getWinner());
}
response.setRound(texasHoldemWithBet.getBetPoker().getRound());
response.setTypePlayer(texasHoldemWithBet.getBetPoker().getTypePlayer());
response.setTypeCasino(texasHoldemWithBet.getBetPoker().getTypeCasino());
response.setPlayerAmount(texasHoldemWithBet.getBetPoker().getPlayerAmount());
response.setCasinoAmount(texasHoldemWithBet.getBetPoker().getCasinoAmount());
if(texasHoldemWithBet.getCasinoCards()!=null) {
    response.setCasinoCards(texasHoldemWithBet.getCasinoCards());
}
if(texasHoldemWithBet.getWinCombination()!=null) {
    response.setWinCombination(texasHoldemWithBet.getWinCombination());
}
return response;
    }
}
