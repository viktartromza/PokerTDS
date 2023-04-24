package com.tromza.pokertds.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RequestUserRegistration {
    @NotNull
    @Pattern(regexp = "[A-z0-9_-]*")
    String login;
    @Size(min = 5)
    String password;
    @Email
    String email;
}
