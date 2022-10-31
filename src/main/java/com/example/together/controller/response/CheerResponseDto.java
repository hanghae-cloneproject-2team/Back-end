package com.example.together.controller.response;

import com.example.together.domain.DonationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheerResponseDto {
  private Long cheerTotalCnt;
  private boolean CheerYn;
}
