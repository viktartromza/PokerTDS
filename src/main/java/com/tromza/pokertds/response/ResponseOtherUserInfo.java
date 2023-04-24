package com.tromza.pokertds.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseOtherUserInfo {
    int id;
    String login;
    double score;
}
