package com.tromza.pokertds.repository;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_games (id, game_id, user_id) VALUES (DEFAULT, :gameId, :userId)", nativeQuery = true)
    void addGameToUser(@Param("gameId") int gameId, @Param("userId") int userId);

    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT user_id FROM users_games WHERE users_games.game_id=:gameId", nativeQuery = true)
    Optional<Integer> findUserIdByGameId(@Param("gameId") int gameId);
    }
