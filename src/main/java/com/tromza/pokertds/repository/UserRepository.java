package com.tms.repository;

import com.tms.domain.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Repository
public class UserRepository {

    public User getUserById(int id) {
        User user = new User();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PokerAppDB", "postgres", "root")) {
            PreparedStatement statementAcc = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            statementAcc.setInt(1, id);
            ResultSet resultSet = statementAcc.executeQuery();

            resultSet.next();
            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
            user.setRegDate(resultSet.getDate("registration_date"));
            user.setScore(resultSet.getInt("score"));

            PreparedStatement statementUserData = connection.prepareStatement("SELECT * FROM users_data WHERE user_id=?");
            statementUserData.setInt(1, id);
            ResultSet resultSetUD = statementUserData.executeQuery();

            resultSetUD.next();
            user.setFirstName(resultSetUD.getString("first_name"));
            user.setLastName(resultSetUD.getString("last_name"));
            user.setBirthDay(resultSetUD.getDate("date_of_birth").toString());
            user.setTelephone(resultSetUD.getString("phone_number"));

        } catch (SQLException e) {
            System.out.println("something wrong....");
        }
        return user;
    }
    public boolean createUser(User user) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PokerAppDB", "postgres", "root")) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (id, login, password, email, registration_date, score) VALUES (DEFAULT, ?, ?, ?, ?, DEFAULT)");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setDate(4, new Date((new java.util.Date()).getTime()));
            statement.executeUpdate();

            PreparedStatement statementId = connection.prepareStatement("SELECT currval('users_id_seq')");
            ResultSet resultSetId = statementId.executeQuery();
            resultSetId.next();
            int id = resultSetId.getInt(1);
            System.out.println(id);

            PreparedStatement statementChanged = connection.prepareStatement("INSERT INTO users_data (user_id, changed) VALUES (?,?)");
            statementChanged.setInt(1, id);
            statementChanged.setTimestamp(2, new Timestamp((new java.util.Date()).getTime()));
            result = statementChanged.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("something wrong....");
        }
        return result == 1;
    }
    public boolean updateUser(User user) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PokerAppDB", "postgres", "root")) {
            PreparedStatement statement = connection.prepareStatement("UPDATE users_data SET first_name=?, last_name=?, country=?, phone_number=?, date_of_birth=?, changed=? WHERE user_id=?");
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getCountry());
            statement.setString(4, user.getTelephone());
            statement.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd/MM/yyyy").parse(user.getBirthDay()).getTime())); //TODO: CHANGE DATE
            statement.setTimestamp(6, new Timestamp((new java.util.Date().getTime())));
            statement.setInt(7, user.getId());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("something wrong....");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result == 1;
    }
}
