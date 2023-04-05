package com.tromza.pokertds.service;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    static UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public static List<User> getAllUsers()  {return userRepository.findAll();}
@Transactional
    public User createUser(User user) {
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        return userRepository.save(user);
    }
@Transactional
    public User updateUser(User user) throws ParseException {
    user.setChanged(new Timestamp(System.currentTimeMillis()));
        return userRepository.saveAndFlush(user);
    }


    public void addGameToUser (User user, Game game) {userRepository.addGameToUser(game.getId(),user.getId());}
    /*
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
}*/
}


