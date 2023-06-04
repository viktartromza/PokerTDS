package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.BetRoulette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<BetRoulette,Integer> {
}
