package com.tromza.pokertds.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@AllArgsConstructor
@Data
@Validated
public class RequestUserUpdate {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String country;
    @NotNull
    private Date birthDay;
    @NotNull
    private String telephone;
}
