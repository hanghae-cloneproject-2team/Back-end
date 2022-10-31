package com.example.together.repository;

import com.example.together.domain.Cheer;
import com.example.together.domain.Member;
import com.example.together.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheerRepository extends JpaRepository<Cheer, Long> {
    Optional<Cheer> findByMemberAndPost(Member member, Post post);
    Long countByPostId(Long postId);
}
