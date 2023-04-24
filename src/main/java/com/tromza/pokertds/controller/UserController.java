package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import com.tromza.pokertds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController // указываем, что контроллер в MVC
@RequestMapping("/users") // указываем, на какой url откликается в HandlerMappinge
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }// конструктор контроллера

    @GetMapping("/scores")
    public ResponseEntity<List<ResponseOtherUserInfo>> getAllUsers() {
        List<ResponseOtherUserInfo> allUsers = userService.getAllUsersForUser();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOtherUserInfo> anotherUserInfo(@PathVariable int id) {
        Optional<ResponseOtherUserInfo> user = userService.otherUserInfo(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/info")
    public ResponseEntity<User> selfUserInfo(Principal principal) {
        Optional<User> user = userService.getUserByLogin(principal.getName());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/registration")
    public ResponseEntity<User> createUser(@RequestBody @Valid RequestUserRegistration userRegistration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o);
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(userService.createUser(userRegistration), HttpStatus.CREATED);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody RequestUserUpdate requestUserUpdate, Principal principal) {
        User user = userService.updateUser(requestUserUpdate, principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteUser(Principal principal) {
        userService.deleteUser(principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/game/{user_id},{game_id}")
    public ResponseEntity<HttpStatus> addGameToUser(@PathVariable int user_id, @PathVariable int game_id) {
        User user = new User();
        Game game = new Game();
        user.setId(user_id);
        game.setId(game_id);
        userService.addGameToUser(user, game);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
