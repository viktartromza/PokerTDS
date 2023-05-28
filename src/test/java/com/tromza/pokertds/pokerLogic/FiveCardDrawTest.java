package com.tromza.pokertds.pokerLogic;

import com.tromza.pokertds.gamesLogic.pokerLogic.FiveCardDraw;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FiveCardDrawTest {

    @Test
    public void processAs2s3s4s5s() {
        double power = FiveCardDraw.powerOfCards("As2s3s4s5s");
        assertTrue(power > 7.00 && power < 8.00);
    }

    @Test
    public void processAh2s3d4s5s() {
        double power = FiveCardDraw.powerOfCards("Ah2s3d4s5s");
        assertTrue(power > 3.00 && power < 4.00);
    }

    @Test
    public void processAh2s3d4sJs() {
        double power = FiveCardDraw.powerOfCards("Ah2s3d4sJs");
        assertTrue(power > 0.14 && power < 0.5);
    }

    @Test
    public void process2h2s3d4sJs() {
        double power = FiveCardDraw.powerOfCards("2h2s3d4sJs");
        assertTrue(power > 0.50 && power < 1.00);
    }

    @Test
    public void process2h2s3d3sJs() {
        double power = FiveCardDraw.powerOfCards("2h2s3d3sJs");
        assertTrue(power > 1.00 && power < 1.99);
    }

    @Test
    public void process2h2s3d3s3h() {
        double power = FiveCardDraw.powerOfCards("2h2s3d3s3h");
        assertTrue(power > 5.00 && power < 5.99);
    }

    @Test
    public void process2hJs3d3s3h() {
        double power = FiveCardDraw.powerOfCards("2hJs3d3s3h");
        assertTrue(power > 2.00 && power < 2.99);
    }

    @Test
    public void process2h3c3d3s3h() {
        double power = FiveCardDraw.powerOfCards("2h3c3d3s3h");
        assertTrue(power > 6.00 && power < 6.99);
    }

    @Test
    public void process2h3hJh5hAh() {
        double power = FiveCardDraw.powerOfCards("2h3hJh5hAh");
        assertTrue(power > 4.00 && power < 4.99);
    }

    @Test
    public void process2h2dJhJdAhVs2c2sJcJsKh() {
        double power2h2dJhJdAh = FiveCardDraw.powerOfCards("2h2dJhJdAh");
        double power2c2sJcJsKh = FiveCardDraw.powerOfCards("2c2sJcJsKh");
        assertTrue(power2h2dJhJdAh > power2c2sJcJsKh);
    }
}
