package com.example.together.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {
  private Long id;
  private String title;
  private String thumbnail;
  private String author;
  private Long priceState; //현재 모인 금액
  private Long price; // 목표금액
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
