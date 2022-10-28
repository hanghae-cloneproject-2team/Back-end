package com.example.together.controller;

import com.example.together.controller.response.ResponseDto;
import com.example.together.controller.request.CommentRequestDto;
import com.example.together.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping(value = "/api/auth/comment")
  public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.createComment(requestDto, request);
  }

  @GetMapping(value = "/api/comment/{id}")
  public ResponseDto<?> getAllComments(@PathVariable Long id) {
    return commentService.getAllCommentsByPost(id);
  }

  @PutMapping(value = "/api/auth/comment/{id}")
  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.updateComment(id, requestDto, request);
  }

  @DeleteMapping(value = "/api/auth/comment/{id}")
  public ResponseDto<?> deleteComment(@PathVariable Long id,
      HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }
}
