package com.tromza.pokertds.repository;


import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

@Repository

public class UserRepository {
    public JdbcTemplate template;

    @Autowired
    public UserRepository(JdbcTemplate template) {
        this.template = template;
    }


    public Optional<User> getUserById(int id) {
        try {
            return Optional.of(template.queryForObject("SELECT u.id, u.login, u.password, u.registration_date, u.email, u.score,ud.first_name,ud.last_name, ud.country, ud.date_of_birth, ud.phone_number  FROM users as u JOIN users_data as ud ON u.id=ud.user_id WHERE u.id=?", new UserMapper(), id));
        } catch (DataAccessException e){
            return Optional.empty();
        }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PokerAppDB", "postgres", "root")) {
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.login, u.password, u.registration_date, u.email, u.score,ud.first_name, ud.last_name, ud.country, ud.date_of_birth, ud.phone_number  FROM users as u JOIN users_data as ud ON u.id=ud.user_id");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                allUsers.add(new User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"),
                        resultSet.getDate("registration_date"), resultSet.getString("email"), resultSet.getInt("score"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("country"), resultSet.getTimestamp("date_of_birth").toString(), resultSet.getString("phone_number")));
            }
        } catch (SQLException e) {
            System.out.println("something wrong....");
        }
        return (ArrayList<User>) template.query("SELECT u.id, u.login, u.password, u.registration_date, u.email, u.score,ud.first_name, ud.last_name, ud.country, ud.date_of_birth, ud.phone_number  FROM users as u JOIN users_data as ud ON u.id=ud.user_id", new UserMapper());
    }

    @Transactional
    public boolean createUser(User user) {

        template.update("INSERT INTO users (id, login, password, email, registration_date, score) VALUES (DEFAULT, ?, ?, ?, ?, DEFAULT)", new Object[]{user.getLogin(), user.getPassword(), user.getEmail(), new Date((new java.util.Date()).getTime())});
        int id = template.queryForObject("SELECT currval('users_id_seq')", Integer.class);
        System.out.println(id);
        int result = template.update("INSERT INTO users_data (user_id, changed) VALUES (?,?)", new Object[]{id, new Timestamp((new java.util.Date()).getTime())});
        return result == 1;
    }

    public boolean updateUser(User user) throws ParseException {
        int result = 0;
        System.out.println(user);
        result = template.update("UPDATE users_data SET first_name=?, last_name=?, country=?, phone_number=?, date_of_birth=?, changed=? WHERE user_id=?", new Object[]{user.getFirstName(), user.getLastName(), user.getCountry(), user.getTelephone(), new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(user.getBirthDay()).getTime()), new Timestamp((new java.util.Date().getTime())), user.getId()});
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
        return result == 0;
    }
}
