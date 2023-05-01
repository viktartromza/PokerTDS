package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status, g.result FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId", nativeQuery = true)
    ArrayList<Game> getGamesForSingleUser(@Param("userId") int id);

    Optional<Game> getGameById(int id);

    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status, g.result FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId AND g.type='ROULETTE_EU' AND g.status='IN_PROCESS'", nativeQuery = true)
    Optional<Game> getRouletteGameInProcess(@Param("userId") int id);

    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status, g.result FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId AND g.type='TEXAS_HOLDEM' AND g.status='IN_PROCESS'", nativeQuery = true)
    Optional<Game> getTexasHoldemGameInProcess(@Param("userId") int id);

    @Query(value = "SELECT u.email FROM users as u JOIN users_games as g ON u.id = g.user_id WHERE g.game_id=:gameId", nativeQuery = true)
    String findEmailByGameId(@Param("gameId") int id);
}
