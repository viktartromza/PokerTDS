package com.tromza.pokertds.utils;

import com.tromza.pokertds.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setRegDate(resultSet.getDate("registration_date"));
        user.setScore(resultSet.getInt("score"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setCountry(resultSet.getString("country"));
        user.setBirthDay(resultSet.getDate("date_of_birth").toString());
        user.setTelephone(resultSet.getString("phone_number"));
        return user;
    }
}
