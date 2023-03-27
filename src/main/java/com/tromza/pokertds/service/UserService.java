package com.tms.service;

import com.tms.annotation.GetTimeAnnotation;
import com.tms.domain.User;
import com.tms.domain.Wallet;
import com.tms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.SimpleBeanInfo;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class UserService {

    static UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
@GetTimeAnnotation
    public static User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public boolean createUser(User user) {
        return userRepository.createUser(user);
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }
    //
    //public boolean deleteUser(int id) {
    //    int result = 0;
    //    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_db", "postgres", "root")) {
    //
    //        PreparedStatement statement = connection.prepareStatement("UPDATE user_table SET is_deleted=true WHERE id=?");
    //        statement.setInt(1, id);
    //        result = statement.executeUpdate();
    //    } catch (SQLException e) {
    //        System.out.println("something wrong....");
    //    }
    //    return result == 1;
    //}
}


