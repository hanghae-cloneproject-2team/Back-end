package com.example.together.service;

import com.example.together.controller.exception.ErrorCode;
import com.example.together.controller.handler.CustomException;
import com.example.together.controller.response.CommentResponseDto;
import com.example.together.controller.response.PostListResponseDto;
import com.example.together.controller.response.PostResponseDto;
import com.example.together.domain.*;
import com.example.together.controller.request.PostRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.jwt.TokenProvider;
import com.example.together.repository.CommentRepository;
import com.example.together.repository.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final FileS3Service fileS3Service;
  private final TokenProvider tokenProvider;

  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile image1,MultipartFile thumbnail, HttpServletRequest request) {
//    if (null == request.getHeader("Refresh-Token")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    // 이미지 파일 처리
    String image1Url = "";

    try {
      image1Url = fileS3Service.uploadFile(image1);
    } catch (IOException e) {
      CustomException.toResponse(new CustomException(ErrorCode.AWS_S3_UPLOAD_FAIL));
    }

    String thumbnailUrl = "";

    try {
      thumbnailUrl = fileS3Service.uploadFile(thumbnail);
    } catch (IOException e) {
      CustomException.toResponse(new CustomException(ErrorCode.AWS_S3_UPLOAD_FAIL));
    }

    Post post = Post.builder()
        .title(requestDto.getTitle())
        .price(requestDto.getPrice())
        .priceState(0L)
        .head1(requestDto.getHead1())
        .content1(requestDto.getContent1())
        .endDate(requestDto.getEndDate())
        .category(requestDto.getCategory())
        .member(member)
        .image1(image1Url)
        .thumbnail(thumbnailUrl)
        .build();

    postRepository.save(post);
    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .author(post.getMember().getNickname())
            .category(post.getCategory())
            .head1(post.getHead1())
            .content1(post.getContent1())
            .image1(post.getImage1())
            .thumbnail(post.getThumbnail())
            .price(post.getPrice())
            .priceState(post.getPriceState())
            .endDate(post.getEndDate())
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      commentResponseDtoList.add(
          CommentResponseDto.builder()
              .id(comment.getId())
              .author(comment.getMember().getNickname())
              .content(comment.getContent())
              .createdAt(comment.getCreatedAt())
              .modifiedAt(comment.getModifiedAt())
              .build()
      );
    }

    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .commentResponseDtoList(commentResponseDtoList)
            .author(post.getMember().getNickname())
            .category(post.getCategory())
            .head1(post.getHead1())
            .content1(post.getContent1())
            .image1(post.getImage1())
            .thumbnail(post.getThumbnail())
            .price(post.getPrice())
            .priceState(post.getPriceState())
            .endDate(post.getEndDate())
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
    List<PostListResponseDto> postListResponseDtoList = new ArrayList<>();
    for (Post post : postList) {
      postListResponseDtoList.add(
              PostListResponseDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .thumbnail(post.getThumbnail())
                      .author(post.getMember().getNickname())
                      .priceState(post.getPriceState())
                      .price(post.getPrice())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
    }

    return ResponseDto.success(postListResponseDtoList);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPostbyCategory(String string){
    Category category = new CategoryConverter().convertToEntityAttribute(string);
    List<Post> postList = postRepository.findAllByCategory(category);
    List<PostListResponseDto> postListResponseDtoList = new ArrayList<>();
    for (Post post : postList) {
      postListResponseDtoList.add(
              PostListResponseDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .thumbnail(post.getThumbnail())
                      .author(post.getMember().getNickname())
                      .priceState(post.getPriceState())
                      .price(post.getPrice())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
    }

    return ResponseDto.success(postListResponseDtoList);
  }

    @Transactional
  public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, MultipartFile image1,MultipartFile thumbnail, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    // 이미지 파일 처리
    String image1Url = "";

    try {
      image1Url = fileS3Service.uploadFile(image1);
    } catch (IOException e) {
      CustomException.toResponse(new CustomException(ErrorCode.AWS_S3_UPLOAD_FAIL));
    }

    String thumbnailUrl = "";

    try {
      thumbnailUrl = fileS3Service.uploadFile(thumbnail);
    } catch (IOException e) {
      CustomException.toResponse(new CustomException(ErrorCode.AWS_S3_UPLOAD_FAIL));
    }

    post.update(requestDto,image1Url,thumbnailUrl );
    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
//                    .commentResponseDtoList(commentResponseDtoList)
                    .author(post.getMember().getNickname())
                    .category(post.getCategory())
                    .head1(post.getHead1())
                    .content1(post.getContent1())
                    .image1(post.getImage1())
                    .thumbnail(post.getThumbnail())
                    .price(post.getPrice())
                    .priceState(post.getPriceState())
                    .endDate(post.getEndDate())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
    );
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }

}
