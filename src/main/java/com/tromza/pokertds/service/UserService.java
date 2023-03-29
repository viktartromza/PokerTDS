package com.tromza.pokertds.service;


import com.tromza.pokertds.annotation.GetTimeAnnotation;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    static UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public static ArrayList<User> getAllUsers()  {return userRepository.getAllUsers();}
    public boolean createUser(User user) {
        return userRepository.createUser(user);
    }

    public boolean updateUser(User user) throws ParseException {
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


