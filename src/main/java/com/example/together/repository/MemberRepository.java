package com.example.together.repository;

import com.example.together.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findById(Long id);
  Optional<Member> findByNickname(String nickname);
  Optional<Member> findByEmailId(String emailId);

}
