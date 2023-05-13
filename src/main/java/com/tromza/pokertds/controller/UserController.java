package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import com.tromza.pokertds.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name="User", description="The User API")
@RestController
@RequestMapping("/users")
public class UserController {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Return list of usernames with id and score")
    @GetMapping("/scores")
    public ResponseEntity<List<ResponseOtherUserInfo>> getAllUsers() {
        List<ResponseOtherUserInfo> allUsers = userService.getAllUsersForUser();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @Operation(summary = "Return info about username and score of user with given id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseOtherUserInfo> anotherUserInfo(@PathVariable int id) {
        Optional<ResponseOtherUserInfo> user = userService.otherUserInfo(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Return info about authenticated user, which made request")
    @GetMapping("/info")
    public ResponseEntity<User> selfUserInfo(Principal principal) {
        Optional<User> user = userService.getUserByLogin(principal.getName());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create new user")
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody @Valid RequestUserRegistration userRegistration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o);
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(userService.createUser(userRegistration), HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Update information about authenticated user")
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody RequestUserUpdate requestUserUpdate, Principal principal) {
        User user = userService.updateUser(requestUserUpdate, principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @Operation(summary = "Change isDeleted status of authenticated user")
    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteUser(Principal principal) {
        userService.deleteUser(principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
