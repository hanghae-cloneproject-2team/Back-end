package com.example.together.domain;

import com.example.together.controller.request.PostRequestDto;
import java.util.List;
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
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private Long price;// 목표금액

  @Column(nullable = false)
  private Long priceState;// 현재금액

  @Column(nullable = true)
  private String thumbnail;// 썸네일

  @Column(nullable = false)
  private String head1;
  @Column(nullable = false)
  private String content1;

  @Column(nullable = true)
  private String image1;

  @Column(nullable = false)
  private String endDate; //기부 종료일

  @Convert(converter = CategoryConverter.class)
  private Category category; //카테고리


  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  public void update(PostRequestDto postRequestDto, String image1, String thumbnail) {
    this.title = postRequestDto.getTitle();
    this.category = postRequestDto.getCategory();
    this.head1=postRequestDto.getHead1();
    this.content1=postRequestDto.getContent1();
    this.price=postRequestDto.getPrice();
    this.endDate=postRequestDto.getEndDate();
    this.image1=image1;
    this.thumbnail=thumbnail;
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

}
