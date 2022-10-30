package com.example.together.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    earth("earth",1),
    friends("friends",2),
    animal("animal",3);

    private  String value;
    private int code;

    Category(String value, int code) {

        this.value = value;
        this.code = code;
    }

    public static Category fromCode(String dbData){
        return Arrays.stream(Category.values())
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("포스트 카테고리에 %s가 존재하지 않습니다.", dbData)));
    }
}
