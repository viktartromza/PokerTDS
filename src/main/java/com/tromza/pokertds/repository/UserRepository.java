package com.tromza.pokertds.repository;


import com.tromza.pokertds.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;
    // public JdbcTemplate template;


    public UserRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        // this.template = template;
    }


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
            Optional<ArrayList<User>> allUsers = Optional.of((ArrayList<User>) query.getResultList());
            return allUsers;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean createUser(User user) {
        //System.out.println(user);
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        Session session = sessionFactory.openSession();
        session.save(user);
        session.close();
        return true;
    }

    public boolean updateUser(User user) {
        String name = user.getFirstName();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setFirstName(name);
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
}
