package com.tromza.pokertds.repository;

import com.tromza.pokertds.domain.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class GameRepository {

    private final SessionFactory sessionFactory;
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
}
