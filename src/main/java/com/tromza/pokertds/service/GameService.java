package com.tromza.pokertds.service;

import com.tromza.pokertds.controller.GameController;
import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.GameStatus;
import com.tromza.pokertds.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GameService {
   GameRepository gameRepository;
@Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void createGame (Game game){
    game.setCreate(new Timestamp(System.currentTimeMillis()));
    game.setStatus(GameStatus.IN_PROCESS);
gameRepository.createGame(game);
    }
}
