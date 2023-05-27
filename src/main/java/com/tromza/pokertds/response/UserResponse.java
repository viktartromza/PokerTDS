package com.tromza.pokertds.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;

    private String login;

    private Date regDate;

    private String email;

    private double score;

    private String firstName;

    private String lastName;

    private String country;

    private Date birthDay;

    private String telephone;

    private Timestamp changed;
}
