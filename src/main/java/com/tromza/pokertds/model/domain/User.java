package com.tromza.pokertds.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "users_data", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq_gen")
    @SequenceGenerator(name = "users_id_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "login", updatable = false)
    private String login;

    @JsonIgnore
    @Column(name = "password", updatable = false)
    private String password;

    @Column(name = "registration_date", updatable = false)
    private Date regDate;

    @Column(name = "email", updatable = false)
    private String email;

    @Column(name = "score")
    private double score;

    @JsonIgnore
    @Column(name = "role")
    private String role;

    @JsonIgnore
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(table = "users_data", name = "first_name")
    private String firstName;

    @Column(table = "users_data", name = "last_name")
    private String lastName;

    @Column(table = "users_data", name = "country")
    private String country;

    @Column(table = "users_data", name = "date_of_birth")
    private Date birthDay;

    @Column(table = "users_data", name = "phone_number")
    private String telephone;

    @JsonIgnore
    @Column(table = "users_data", name = "changed")
    private Timestamp changed;
}

