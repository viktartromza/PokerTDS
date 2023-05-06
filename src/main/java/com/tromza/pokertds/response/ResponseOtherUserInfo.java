package com.tromza.pokertds.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOtherUserInfo {
    int id;
    String login;
    double score;
}
