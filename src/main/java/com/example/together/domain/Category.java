package com.example.together.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    지구촌("지구촌",1),
    어려운이웃("어려운이웃",2),
    동물("동물",3);

    private  String value;
    private int code;

    Category(String value, int code) {

        this.value = value;
        this.code = code;
    }
}
