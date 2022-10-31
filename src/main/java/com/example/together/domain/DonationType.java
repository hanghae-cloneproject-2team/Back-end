package com.example.together.domain;

public enum DonationType {
    C(0, "C"),
    D(1, "D");

    private int code;
    private String value;

    DonationType(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
