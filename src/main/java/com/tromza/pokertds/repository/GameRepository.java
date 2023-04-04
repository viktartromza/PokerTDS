package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Integer> {

    @Query(value = "SELECT g.id, g.type, g.time_create, g.finish, g.status FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId", nativeQuery = true)
    Optional<ArrayList<Game>> getGamesForSingleUser(@Param("userId") int id);

   /* private final SessionFactory sessionFactory;
    // public JdbcTemplate template;


    public GameRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        // this.template = template;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void createGame(Game game) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        System.out.println(game);
        session.save(game);
        session.getTransaction().commit();
        log.info("Game with id: " + game.getId() + " created at " + new Date(System.currentTimeMillis()));
        session.close();
    }

    */
}
