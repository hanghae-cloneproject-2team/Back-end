package com.example.together.controller.request;

import com.example.together.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
  private String title;
  private Category category;
  private String head1;
  private String content1;
  private Long price;
  private String endDate;
}
