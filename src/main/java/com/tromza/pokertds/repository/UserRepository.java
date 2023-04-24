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

    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId", nativeQuery = true)
    Optional<ArrayList<Game>> getGamesForSingleUser(@Param("userId") int id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_games (id, game_id, user_id) VALUES (DEFAULT, :gameId, :userId)", nativeQuery = true)
    void addGameToUser(@Param("gameId") int gameId, @Param("userId") int userId);

    Optional<User> findUserByLogin(String login);
    }
