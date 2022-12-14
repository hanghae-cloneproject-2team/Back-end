package com.example.together.controller.response;

import java.time.LocalDateTime;

import com.example.together.domain.DonationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
  private Long id;
  private Long donation;
  private String comment;
  private String nickname;
  private DonationType donationType;
  private LocalDateTime createdAt;
}
