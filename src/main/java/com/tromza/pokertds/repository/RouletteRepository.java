package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.RouletteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface RouletteRepository extends JpaRepository <RouletteGame,Integer> {

Optional<RouletteGame> findRouletteGameByGameId (int id);

    Optional<RouletteGame> findRouletteGameById (int id);

    @Query(value = "SELECT r.id, r.game_id, r.spin, r.wins, r.losses, r.result, r.status, r.changed FROM roulette_games as r WHERE r.status='IN_PROCESS' AND r.changed<=:time", nativeQuery = true)
    ArrayList<RouletteGame> findAllByStatusIsInProcessAndChangedBefore(@Param("time") Timestamp time);
    @Query(value = "SELECT u.email FROM users as u JOIN users_games as g ON u.id = g.user_id WHERE g.game_id=:gameId", nativeQuery = true)
    String findEmailByGameId (@Param("gameId") int id);
}
