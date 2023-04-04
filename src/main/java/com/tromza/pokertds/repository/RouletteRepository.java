package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.RouletteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouletteRepository extends JpaRepository <RouletteGame,Integer> {


}
