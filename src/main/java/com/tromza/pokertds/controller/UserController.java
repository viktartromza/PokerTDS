package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;


@RestController // указываем, что контроллер в MVC
@RequestMapping("/user") // указываем, на какой url откликается в HandlerMappinge
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }// конструктор контроллера

    @GetMapping
    public ArrayList<User> getAllUsers() {
        return UserService.getAllUsers();
    }

    @GetMapping("/{id}")// указываем на какой метод откликается в HandlerMappinge и /доп для значения @PathVariable
    public User getUserById(@PathVariable int id) {
        //    log.info("doing/user Get method!");
        return UserService.getUserById(id);
    }

    //@ResponseStatus(value=HttpStatus.CREATED)
    @PostMapping("/create")
    public void createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o);
            }
        }
        else userService.createUser(user);
    }

    @PutMapping("/update")
    public void updateUser(@RequestBody User user) throws ParseException {
        userService.updateUser(user);
    }
}