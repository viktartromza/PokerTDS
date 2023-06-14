package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.BetPoker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokerBetRepository extends JpaRepository<BetPoker, Integer> {

    @Query(value = "SELECT * FROM bets_poker WHERE game_id=:id ORDER BY round DESC LIMIT 1", nativeQuery = true)
    Optional<BetPoker> findLastBetPokerByGameId (@Param("id") int id);
}
