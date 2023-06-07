package com.tromza.pokertds.restAssuredTests;

import com.tromza.pokertds.model.request.AuthRequest;

import com.tromza.pokertds.model.request.UserMoneyAmountRequest;
import com.tromza.pokertds.model.response.UserResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;


public class AdminControllerTests {
    private final static String URL = "http://localhost:8080/admin";
    private final static String AUTHURL = "http://localhost:8080/auth";
    private String token;

    @BeforeEach
    public void Auth() {
        token = "Bearer " + RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body(new AuthRequest("admin", "admin"))
                .post(AUTHURL)
                .then()
                .extract().response().jsonPath().getString("token");
    }

    @Test
    public void checkGetAllPresentUsers() {
        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .get(URL + "/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void checkGetAllDeletedUsers() {
        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .get(URL + "/delusers")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Ignore
    public void checkDeleteAndCancelDeleteUserByIdForAdmin() {
        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .delete(URL + "/users" + "/1")
                .then()
                .log().all()
                .statusCode(204);

        UserResponse deletedUser = RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .get(URL + "/delusers")
                .then()
                .log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", UserResponse.class).get(0);

        Assertions.assertEquals("testUser", deletedUser.getLogin());

        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .put(URL + "/delusers" + "/1")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void checkTransferWallet() {
        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(new UserMoneyAmountRequest(1, BigDecimal.valueOf(200)))
                .put(URL + "/wallets")
                .then()
                .log().all()
                .statusCode(200)
                .body("userId", equalTo(1));

        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(new UserMoneyAmountRequest(1, BigDecimal.valueOf(100).negate()))
                .put(URL + "/wallets")
                .then()
                .log().all()
                .statusCode(200)
                .body("userId", equalTo(1));
    }
}
