package com.example.together.controller;

import com.example.together.controller.request.LoginRequestDto;
import com.example.together.controller.request.MemberRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;

  //회원가입
  @PostMapping(value = "/api/member/signup")
  public ResponseDto<?> signup(@Valid @RequestBody MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }
  //로그인
  @PostMapping(value = "/api/member/login")
  public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }
  //로그아웃
  @PostMapping(value = "/api/auth/member/logout")
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }

  //토큰 재발급
  @RequestMapping(value = "api/member/reissue", method = RequestMethod.POST)
  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    return memberService.reissue(request, response);
  }

}
