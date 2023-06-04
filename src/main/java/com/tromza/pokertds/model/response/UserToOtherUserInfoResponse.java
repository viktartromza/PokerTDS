package com.tromza.pokertds.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToOtherUserInfoResponse {
    int id;
    String login;
    double score;
}
