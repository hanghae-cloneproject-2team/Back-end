package com.example.intermediate.service;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return (UserDetails) new Member(); // 이 부분을 지우고 기능을 구현해주세요:)
  }
}
