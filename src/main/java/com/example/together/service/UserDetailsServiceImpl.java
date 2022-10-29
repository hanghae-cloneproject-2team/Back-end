package com.example.together.service;

import com.example.together.controller.exception.ErrorCode;
import com.example.together.controller.handler.CustomException;
import com.example.together.domain.Member;
import com.example.together.domain.UserDetailsImpl;
import com.example.together.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> member = memberRepository.findByNickname(username);
    return member
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
  }
}
