package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.RouletteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouletteRepository extends JpaRepository <RouletteGame,Integer> {

Optional<RouletteGame> findRouletteGameByGameId (int id);
}
