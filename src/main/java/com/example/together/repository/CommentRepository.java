package com.example.together.repository;

import com.example.together.domain.Comment;
import com.example.together.domain.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Long countByPost(Post post);

    // 직접기부한 사람 수 ( postId , donation_type = 'D')
    @Query(value = "select count(post_id) as cnt from comment where post_id = :post_id and donation_type = 'D'", nativeQuery = true)
    Long selectDirectDonationCnt(@Param("post_id") Post post);

    // 직접기부금 총 금액 ( postId , donation_type = 'D')
    @Query(value = "select sum(donation) as total from comment where post_id = :post_id and donation_type = 'D'", nativeQuery = true)
    Long selectDirectDonationSum(@Param("post_id") Post post);

    // 댓글기부한 사람 수 ( postId , donation_type = 'C')
    @Query(value = "select count(post_id) as cnt from comment where post_id = :post_id and donation_type = 'C'", nativeQuery = true)
    Long selectCommentDonationCnt(@Param("post_id") Post post);

    // 댓글기부금 총 금액 ( postId , donation_type = 'C')
    @Query(value = "select sum(donation) as total from comment where post_id = :post_id and donation_type = 'C'", nativeQuery = true)
    Long selectCommentDonationSum(@Param("post_id") Post post);

}
