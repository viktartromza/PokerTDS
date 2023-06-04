package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.BetPoker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokerBetRepository extends JpaRepository<BetPoker, Integer> {
}
