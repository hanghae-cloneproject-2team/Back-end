package com.example.together.controller;

import com.example.together.controller.response.ResponseDto;
import com.example.together.controller.request.CommentRequestDto;
import com.example.together.service.CommentService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CommentController {

  private final CommentService commentService;

  @PostMapping(value = "/comment")
  public ResponseDto<?> writeComment(@RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.writeComment(requestDto, request);
  }

//  @GetMapping(value = "/api/comment/{id}")
//  public ResponseDto<?> getAllComments(@PathVariable Long id) {
//    ret  urn commentService.getAllCommentsByPost(id);
//  }

  @DeleteMapping(value = "/comment/{commentId}")
  public ResponseDto<?> deleteComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
    return commentService.deleteComment(commentId, requestDto, request);
  }
}
