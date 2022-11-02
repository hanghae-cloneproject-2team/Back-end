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
  private Long commentCount; // 댓글 갯수
  private Long directDnCnt; //직접기부한 사람 수
  private Long directDnTotal; // 직접기부금 총 금액
  private Long participateDnCnt; // 참여기부한 사람 수
  private Long participateDnTotal; // 참여기부금 총 금액
  private Long cheerDnCnt; // 응원기부한 사람 수
  private Long cheerDnTotal; // 응원기부금 총 금액
  private Long commentDnCnt; // 댓글기부한 사람 수
  private Long commentDnTotal; // 댓글기부금 총 금액
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
