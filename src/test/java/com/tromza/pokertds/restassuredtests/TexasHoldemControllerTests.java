package com.tromza.pokertds.restassuredtests;

import com.tromza.pokertds.model.request.AuthRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TexasHoldemControllerTests {
    private final static String URL = "http://localhost:8080/games/poker/texas";
    private final static String AUTHURL = "http://localhost:8080/auth";
    private String token;

    @BeforeEach
    public void Auth() {
        token = "Bearer " + RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body(new AuthRequest("testUser", "testpass"))
                .post(AUTHURL)
                .then()
                .extract().response().jsonPath().getString("token");
    }

    @Test
    public void checkCreateTexasHoldem(){
        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .post(URL)
                .then()
                .log().all()
                .statusCode(201);
    }
}
