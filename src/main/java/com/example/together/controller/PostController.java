package com.example.together.controller;

import com.example.together.controller.request.PostRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.PostService;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


  @PostMapping(value = "/api/posting",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
  public ResponseDto<?> createPost(@RequestPart(value = "postDto") PostRequestDto requestDto,
                                   @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                   @RequestParam(value = "image1", required = false) MultipartFile image1,
                                   HttpServletRequest request) {
    return postService.createPost(requestDto,image1, thumbnail, request);
  }

  @GetMapping(value = "/api/posting/{postingId}")
  public ResponseDto<?> getPost(@PathVariable Long postingId) {
    return postService.getPost(postingId);
  }

  @GetMapping(value = "/api/posting")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }


  @PutMapping(value = "/api/posting/{postingId}",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
  public ResponseDto<?> updatePost(@PathVariable Long postingId,
                                   @RequestPart(value = "postDto") PostRequestDto requestDto,
                                   @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
                                   @RequestParam(value = "image1", required = false) MultipartFile image1,
      HttpServletRequest request) {
    return postService.updatePost(postingId, requestDto, image1, thumbnail, request);
  }

  @DeleteMapping(value = "/api/posting/{postingId}")
  public ResponseDto<?> deletePost(@PathVariable Long postingId,
      HttpServletRequest request) {
    return postService.deletePost(postingId, request);
  }

}
