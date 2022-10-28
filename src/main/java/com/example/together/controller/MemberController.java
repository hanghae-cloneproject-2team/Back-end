package com.example.together.controller;

import com.example.together.controller.request.LoginRequestDto;
import com.example.together.controller.request.MemberRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;

  @PostMapping(value = "/api/member/signup")
  public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }

  @PostMapping(value = "/api/member/login")
  public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

  @PostMapping(value = "/api/auth/member/logout")
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }
}
