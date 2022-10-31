package com.example.together.domain;

import com.example.together.controller.request.CommentRequestDto;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "post_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Post post;

  @Column(nullable = false)
  private Long donation;

  @Column(length = 500)
  private String comment;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private DonationType donationType;


  public void update(CommentRequestDto commentRequestDto) {
    this.comment = commentRequestDto.getComment();
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
