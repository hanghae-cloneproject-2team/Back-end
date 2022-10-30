package com.example.together.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.together.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String author;
  private Category category;
  private String head1;
  private String content1;
  private String image1;
  private String thumbnail;
  private Long price;
  private Long priceState;
  private String endDate;
  private List<CommentResponseDto> commentResponseDtoList;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
