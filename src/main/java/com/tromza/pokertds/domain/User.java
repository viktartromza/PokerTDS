package com.tromza.pokertds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;

@Component
@Data
@Entity
@Table(name = "users")
@SecondaryTables({
        @SecondaryTable( name = "users_data"),
        @SecondaryTable( name="wallets"),})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "login")
    @Pattern(regexp = "[A-z0-9_-]*")
    private String login;
    @Size(min = 5)
    @Column(name = "password")
    //@JsonIgnore
    private String password;
    @Column(name = "registration_date")
    private Date regDate;
    @Email
    @Column(name = "email")
    private String email;
    @Column(name = "score")
    private int score;
@Column(table = "users_data",name = "first_name")
    private String firstName;
    @Column(table = "users_data",name = "last_name")
    private String lastName;
    @Column(table = "users_data",name = "country")
    private String country;
    @Column(table = "users_data",name = "date_of_birth")
    private String birthDay;
    @Column(table = "users_data",name = "phone_number")
    private String telephone;

@Column(table = "wallets")
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

    public User(int id, String login, String password, Date regDate, String email, int score, String firstName, String lastName, String country, String birthDay, String telephone) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.regDate = regDate;
        this.email = email;
        this.score = score;
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

