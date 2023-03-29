package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;


@RestController // указываем, что контроллер в MVC
@RequestMapping("/user") // указываем, на какой url откликается в HandlerMappinge
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }// конструктор контроллера
/*
    @GetMapping
    public ResponseEntity<ArrayList<User>> getAllUsers() {
        return new ResponseEntity<>(UserService.getAllUsers(), HttpStatus.ALREADY_REPORTED);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = UserService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.ALREADY_REPORTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
/*
    //@ResponseStatus(value=HttpStatus.CREATED)
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o);
            }
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else if(userService.createUser(user)){
        return  new ResponseEntity<>(HttpStatus.CREATED);}
        else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) throws ParseException {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}*/
}