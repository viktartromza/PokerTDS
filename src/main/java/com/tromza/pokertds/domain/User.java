package com.tromza.pokertds.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;

@Component
@Data
public class User {
    private int id;
    @Pattern(regexp = "[A-z0-9_-]*")
    private String login;
    @Size(min = 5)
    private String password;
    private Date regDate;
    @Email
    private String email;
    private int score;

    private String firstName;
    private String lastName;
    private String country;
    private String birthDay;
    private String telephone;


    private Wallet wallet;

    public User() {
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public User(int id, String firstName, String lastName, String country, String birthDay, String telephone) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.birthDay = birthDay;
        this.telephone = telephone;
    }

    public User(Wallet wallet) {
        this.wallet = wallet;
    }

    @Autowired
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}

