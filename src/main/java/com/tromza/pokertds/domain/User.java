package com.tromza.pokertds.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "users_data",pkJoinColumns=@PrimaryKeyJoinColumn(name="user_id"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq_gen")
    @SequenceGenerator(name = "users_id_seq_gen", sequenceName = "users_table_id_seq", allocationSize = 1)
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
    @Column(table = "users_data", name = "changed")
    private Timestamp changed;
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private Wallet wallet;
}

