package com.tromza.pokertds.restassuredtests;

import com.tromza.pokertds.model.request.AuthRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class AuthControllerTests {
    private final static String URL = "http://localhost:8080/auth";

    @Test
    public void checkPostAuth() {
        RestAssured.given()
                .log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(new AuthRequest("testUser", "testpass"))
                .post(URL)
                .then()
                .statusCode(201);
    }
}
