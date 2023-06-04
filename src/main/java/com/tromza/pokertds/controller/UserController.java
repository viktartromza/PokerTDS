package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.UserFacade;
import com.tromza.pokertds.model.request.UserRegistrationRequest;
import com.tromza.pokertds.model.request.UserUpdateRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.UserToOtherUserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserFacade userFacade;

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Operation(summary = "Return list of usernames with id and score")
    @GetMapping("/scores")
    public ResponseEntity<List<UserToOtherUserInfoResponse>> getAllUsers() {
        List<UserToOtherUserInfoResponse> allUsers = userFacade.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @Operation(summary = "Return info about username and score of user with given id")
    @GetMapping("/{id}")
    public ResponseEntity<UserToOtherUserInfoResponse> anotherUserInfo(@PathVariable int id) {
       UserToOtherUserInfoResponse user = userFacade.anotherUserInfoById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Return info about authenticated user, which made request")
    @GetMapping("/info")
    public ResponseEntity<UserResponse> selfUserInfo(Principal principal) {
        UserResponse user = userFacade.selfUserInfo(principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Create new user")
    @ApiResponse(content = @Content(schema = @Schema(implementation = UserResponse.class)))
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRegistrationRequest userRegistration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError o : bindingResult.getAllErrors()) {
                log.warn("We have validation error: " + o);
                errors.add(o.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(userFacade.createUser(userRegistration), HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Update information about authenticated user")
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, Principal principal) {
        UserResponse user = userFacade.updateUser(userUpdateRequest, principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Change isDeleted status of authenticated user")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(Principal principal) {
        userFacade.deleteUser(principal);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
