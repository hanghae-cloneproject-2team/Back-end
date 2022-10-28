package com.example.together.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    지구촌("지구촌"),
    어려운이웃("어려운이웃"),
    동물("동물");

    private  String value;

    Category(String value) {
        this.value = value;
    }
}
