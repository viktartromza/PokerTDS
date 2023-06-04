package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.RouletteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface RouletteRepository extends JpaRepository<RouletteGame, Integer> {

    Optional<RouletteGame> findRouletteGameByGameId(int id);

    @Query(value = "SELECT * FROM roulette_games as r WHERE r.status='IN_PROCESS' AND r.changed<=:time", nativeQuery = true)
    ArrayList<RouletteGame> findAllByStatusIsInProcessAndChangedBefore(@Param("time") Timestamp time);
}
