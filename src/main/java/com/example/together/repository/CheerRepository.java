package com.example.together.repository;

import com.example.together.domain.Cheer;
import com.example.together.domain.Member;
import com.example.together.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheerRepository extends JpaRepository<Cheer, Long> {
    Optional<Cheer> findByMemberAndPost(Member member, Post post);
    Long countByPostId(Long postId);

    //응원기부한 사람 수 ( postId )
    @Query(value = "select count(post_id) as cnt from cheer where post_id = :post_id", nativeQuery = true)
    Long selectCheerDonationCnt(@Param("post_id") Post post);

    // 응원기부금 총 금액( postId )
    @Query(value = "select sum(donation) as total from cheer where post_id = :post_id", nativeQuery = true)
    Long selecCheerDonationSum(@Param("post_id") Post post);

}
