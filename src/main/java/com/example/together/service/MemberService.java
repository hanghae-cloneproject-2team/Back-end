package com.example.together.service;

import com.example.together.controller.response.MemberResponseDto;
import com.example.together.domain.Member;
import com.example.together.controller.request.LoginRequestDto;
import com.example.together.controller.request.MemberRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.controller.request.TokenDto;
import com.example.together.domain.RefreshToken;
import com.example.together.domain.UserDetailsImpl;
import com.example.together.error.ErrorCode;
import com.example.together.jwt.TokenProvider;
import com.example.together.repository.MemberRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.together.repository.RefreshTokenRepository;
import com.example.together.shared.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRepository refreshTokenRepository;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final TokenProvider tokenProvider;

//  private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

  //회원가입
  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto) {

    //중복된 이메일일 경우
    if (null != isPresentMember(requestDto.getEmailId())) {
      return ResponseDto.fail(ErrorCode.DUPLICATED_EMAIL.name(),
              ErrorCode.DUPLICATED_EMAIL.getMessage());
    }
    //중복된 닉네임일 경우
    if (null != isPresentMember(requestDto.getNickname())) {
      return ResponseDto.fail(ErrorCode.ALREADY_SAVED_NICKNAME.name(),
              ErrorCode.ALREADY_SAVED_NICKNAME.getMessage());
    }
    //비밀번호가 일치하지 않을 경우
    if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
      return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED.name(),
              ErrorCode.PASSWORDS_NOT_MATCHED.getMessage());
    }

    Member member = Member.builder()
                .emailId(requestDto.getEmailId())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
//              .role(Authority.ROLE_MEMBER)
                    .build();

//    if(requestDto.isAdmin()){
//      if(!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
//        return ResponseDto.fail("NOT_ADMIN", "ADMIN토큰이 일치하지 않습니다.");
//      }
//      Member.builder()
//              .nickname(requestDto.getNickname())
//              .name(requestDto.getName())
//              .password(passwordEncoder.encode(requestDto.getPassword()))
//              .role(Authority.ROLE_ADMIN)
//              .build();
//    }

    memberRepository.save(member);

    return ResponseDto.success(
        MemberResponseDto.builder()
            .id(member.getId())
            .emailId(member.getEmailId())
            .nickname(member.getNickname())
            .createdAt(member.getCreatedAt())
            .modifiedAt(member.getModifiedAt())
            .build()
    );
  }

  //로그인
  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
      Member member = isPresentMember(requestDto.getEmailId());

      //이메일(아이디)가 틀렸을 경우 Error 메세지 전송
      if (null == member) {
        return ResponseDto.fail(ErrorCode.INVALID_MEMBER.name(),
                ErrorCode.INVALID_MEMBER.getMessage());
      }
      //패스워드가 틀렸을 경우 Error 메세지 전송
      if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
      return ResponseDto.fail(ErrorCode.INVALID_PASSWORD.name(), ErrorCode.INVALID_PASSWORD.getMessage());
      }

      TokenDto tokenDto = tokenProvider.generateTokenDto(member);
      tokenToHeaders(tokenDto, response);

      return ResponseDto.success(
                MemberResponseDto.builder()
                    .id(member.getId())
                    .emailId(member.getEmailId())
                    .nickname(member.getNickname())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getModifiedAt())
                    .build()
            );
          }

  //로그아웃
  public ResponseDto<?> logout(HttpServletRequest request) {

    //토큰이 유효하지 않을 경우 Error 메세지 전송
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
    }
    Member member = tokenProvider.getMemberFromAuthentication();

    //사용자를 찾을 수 없는 경우 Error 메세지 전송
    if (null == member) {
      return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND.name(),
              ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }
    return tokenProvider.deleteRefreshToken(member);
  }

  //토큰재발급
  @Transactional
  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    System.out.println("request.getHeader(\"Refresh-Token\")!!!= " + request.getHeader("Refresh-Token"));
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage());
    }


//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access_Token"));
//    Member member = ((UserDetailsImpl) authentication.getPrincipal()).getMember();
    Member member = refreshTokenRepository.findByValue(request.getHeader("Refresh-Token")).get().getMember();
    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);



    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
    refreshToken.updateValue(tokenDto.getRefreshToken());
    tokenToHeaders(tokenDto, response);
    return ResponseDto.success("success");
  }

  @Transactional(readOnly = true)
  public Member isPresentMember(String emailID) {
    Optional<Member> optionalMember = memberRepository.findByEmailId(emailID);
    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

  //토큰이 유효하다면 토큰 값 사용
  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }


}
