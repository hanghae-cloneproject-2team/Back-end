package com.example.together.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheerRequestDto {
  private Long postId;
  private Long donation = Long.valueOf(100);
}
