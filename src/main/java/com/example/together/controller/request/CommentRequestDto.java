package com.example.together.controller.request;

import com.example.together.domain.DonationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
  private Long postId;
  private Long donation = Long.valueOf(100);
  private String nickname;
  private String comment = "secret";
  private DonationType donationType;
}
