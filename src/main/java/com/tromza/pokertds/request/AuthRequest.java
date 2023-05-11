package com.tromza.pokertds.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
