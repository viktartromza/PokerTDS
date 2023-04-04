package com.tromza.pokertds.repository;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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

    public Optional<User> getUserById(int id) {
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery("from User u where u.id=:userId");
            query.setParameter("userId", id);
            Optional<User> user = Optional.ofNullable((User) query.getSingleResult());
            return user;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<ArrayList<User>> getAllUsers() {
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery("from User");
            return Optional.of((ArrayList<User>) query.getResultList());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean createUser(User user) {
        System.out.println(user);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        System.out.println(user);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        System.out.println(user);
        session.close();
        return true;
    }

    public boolean updateUser(User user) {
        //String name = user.getFirstName();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        //user.setFirstName(name);
        session.update(user);
        session.getTransaction().commit();
        session.close();
        return true;
        // int result = 0;
        // System.out.println(user);
        // result = template.update("UPDATE users_data SET first_name=?, last_name=?, country=?, phone_number=?, date_of_birth=?, changed=? WHERE user_id=?", new Object[]{user.getFirstName(), user.getLastName(), user.getCountry(), user.getTelephone(), new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthDay()).getTime()), new Timestamp((new java.util.Date().getTime())), user.getId()});
        //try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PokerAppDB", "postgres", "root")) {
        // PreparedStatement statement = connection.prepareStatement("UPDATE users_data SET first_name=?, last_name=?, country=?, phone_number=?, date_of_birth=?, changed=? WHERE user_id=?");
        // statement.setString(1, user.getFirstName());
        // statement.setString(2, user.getLastName());
        // statement.setString(3, user.getCountry());
        // statement.setString(4, user.getTelephone());
        // statement.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(user.getBirthDay()).getTime())); //TODO: CHANGE DATE without sec
        // statement.setTimestamp(6, new Timestamp((new java.util.Date().getTime())));
        // statement.setInt(7, user.getId());
        // statement.executeUpdate();

        // PreparedStatement statementLogin = connection.prepareStatement("SELECT login FROM users WHERE id=?");
        // statementLogin.setInt(1, user.getId());
        // ResultSet resultSet = statementLogin.executeQuery();
        // resultSet.next();
        // result = resultSet.getString("login");
        //} catch (SQLException e) {
        //   System.out.println("something wrong....");
        //} catch (ParseException e) {
        //   throw new RuntimeException(e);
        // }


    }

    public Optional<ArrayList<Game>> getGamesForSingleUser(User user) {
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createNativeQuery("SELECT g.id, g.type, g.time_create, g.finish, g.status FROM users_games JOIN games as g ON users_games.game_id = g.id WHERE users_games.user_id=:userId");
            query.setParameter("userId", user.getId());
            Optional<ArrayList<Game>> games = Optional.of((ArrayList<Game>) query.getResultList());
            session.close();
            return games;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

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
