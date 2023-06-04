package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.TexasHoldemGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface TexasHoldemRepository extends JpaRepository<TexasHoldemGame,Integer> {
    Optional<TexasHoldemGame> findTexasHoldemGameByGameId(int id);

    @Query(value = "SELECT * FROM texasholdem_games as t WHERE t.status='IN_PROCESS' AND t.changed<=:time", nativeQuery = true)
    ArrayList<TexasHoldemGame> findAllByStatusIsInProcessAndChangedBefore(@Param("time") Timestamp time);
}
