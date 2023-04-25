package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.domain.TexasHoldemGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TexasHoldemRepository extends JpaRepository<TexasHoldemGame,Integer> {
    Optional<TexasHoldemGame> findTexasHoldemGameByGameId(int id);
}
