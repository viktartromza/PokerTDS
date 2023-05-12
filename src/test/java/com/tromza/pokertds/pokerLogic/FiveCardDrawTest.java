package com.tromza.pokertds.pokerLogic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FiveCardDrawTest {

    @Test
    public void processAs2s3s4s5s() {
        double power = FiveCardDraw.process("As2s3s4s5s");
        assertTrue(power > 7.00 && power < 8.00);
    }

    @Test
    public void processAh2s3d4s5s() {
        double power = FiveCardDraw.process("Ah2s3d4s5s");
        assertTrue(power > 3.00 && power < 4.00);
    }

    @Test
    public void processAh2s3d4sJs() {
        double power = FiveCardDraw.process("Ah2s3d4sJs");
        assertTrue(power > 0.14 && power < 0.5);
    }

    @Test
    public void process2h2s3d4sJs() {
        double power = FiveCardDraw.process("2h2s3d4sJs");
        assertTrue(power > 0.50 && power < 1.00);
    }

    @Test
    public void process2h2s3d3sJs() {
        double power = FiveCardDraw.process("2h2s3d3sJs");
        assertTrue(power > 1.00 && power < 1.99);
    }

    @Test
    public void process2h2s3d3s3h() {
        double power = FiveCardDraw.process("2h2s3d3s3h");
        assertTrue(power > 5.00 && power < 5.99);
    }

    @Test
    public void process2hJs3d3s3h() {
        double power = FiveCardDraw.process("2hJs3d3s3h");
        assertTrue(power > 2.00 && power < 2.99);
    }

    @Test
    public void process2h3c3d3s3h() {
        double power = FiveCardDraw.process("2h3c3d3s3h");
        assertTrue(power > 6.00 && power < 6.99);
    }

    @Test
    public void process2h3hJh5hAh() {
        double power = FiveCardDraw.process("2h3hJh5hAh");
        assertTrue(power > 4.00 && power < 4.99);
    }

}
