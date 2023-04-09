package com.khc.enrollmentFront.type;

public enum ScoreType {
    A_PLUS(4.5, "A+"),
    A_ZERO(4.0, "A0"),
    B_PLUS(3.5, "B+"),
    B_ZERO(3.0, "B0"),
    C_PLUS(2.5, "C+"),
    C_ZERO(2.0, "C0"),
    D_PLUS(1.5, "D+"),
    D_ZERO(1.0, "D0"),
    F(0.0, "F"),

    PASS(4.5, "P"),
    NO_PASS(0.0, "NP");

    double digit;
    String str;

    public double getDigit() {
        return digit;
    }

    public String getStr() {
        return str;
    }

    ScoreType(double digit, String code) {
        this.digit = digit;
        this.str = code;
    }
}
