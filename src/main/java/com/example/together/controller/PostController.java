package com.example.together.controller;

import com.example.together.controller.request.PostRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.PostService;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @ApiImplicitParams({
          @ApiImplicitParam(
                  name = "Refresh-Token",
                  required = true,
                  dataType = "string",
                  paramType = "header"
          )
  })

  @PostMapping(value = "/api/posting")
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
      HttpServletRequest request) {
    return postService.createPost(requestDto, request);
  }

  @GetMapping(value = "/api/posting/{postingId}")
  public ResponseDto<?> getPost(@PathVariable Long postingId) {
    return postService.getPost(postingId);
  }

  @GetMapping(value = "/api/posting")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }

  @PutMapping(value = "/api/posting/{postingId}")
  public ResponseDto<?> updatePost(@PathVariable Long postingId, @RequestBody PostRequestDto postRequestDto,
      HttpServletRequest request) {
    return postService.updatePost(postingId, postRequestDto, request);
  }

  @DeleteMapping(value = "/api/posting/{postingId}")
  public ResponseDto<?> deletePost(@PathVariable Long postingId,
      HttpServletRequest request) {
    return postService.deletePost(postingId, request);
  }

}
