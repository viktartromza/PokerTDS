package com.tromza.pokertds.restAssuredTests;

import com.tromza.pokertds.model.enums.BetType;
import com.tromza.pokertds.model.request.AuthRequest;
import com.tromza.pokertds.model.request.BetRouletteRequest;
import com.tromza.pokertds.model.response.RouletteResponse;
import com.tromza.pokertds.model.response.RouletteWithBetResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class RouletteControllerTests {
    private final static String URL = "http://localhost:8080/games/roulette";
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
    public void checkCreateRouletteAndplayingGameAndFinissRouletteGameById() {
        RouletteResponse rouletteGame = RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .post(URL)
                .then()
                .log().all()
                .statusCode(201)
                .extract().response().as(RouletteResponse.class);

        RouletteWithBetResponse rouletteWithBetResponse = RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(new BetRouletteRequest(rouletteGame.getGameId(), BetType.NUMBER, BigDecimal.valueOf(5.00),"0"))
                .put(URL)
                .then()
                .log().all()
                .statusCode(202)
                .extract().body().as(RouletteWithBetResponse.class);
        Assertions.assertTrue(rouletteWithBetResponse.getRouletteNumber()>=0&&rouletteWithBetResponse.getRouletteNumber()<=36);

        RestAssured
                .given()
                .when()
                .log().all()
                .header("Authorization", token)
                .pathParam("id", rouletteGame.getId())
                .put(URL+"/finish/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }
}
