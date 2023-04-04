package com.tromza.pokertds.repository;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /*private final SessionFactory sessionFactory;
    // public JdbcTemplate template;


    public UserRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        // this.template = template;
    }
    private final Logger log = LoggerFactory.getLogger(this.getClass());

*/
    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId", nativeQuery = true)
        Optional<ArrayList<Game>> getGamesForSingleUser(@Param("userId") int id);
/*
    public void addGameToUser (User user, Game game){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createNativeQuery("INSERT INTO users_games (id, game_id, user_id) VALUES (DEFAULT, :gameId, :userId)");
        query.setParameter("gameId",game.getId());
        query.setParameter("userId", user.getId());
        int result = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        log.info("Game with id: " + game.getId() + " added to user with id:" + user.getId() + "result: "+ result);
    }
*/
}
