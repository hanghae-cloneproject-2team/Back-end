package com.example.together.controller;

import com.example.together.controller.request.CommentRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class DonationController {
    private final CommentService commentService;

    @PostMapping(value = "/donation")
    public ResponseDto<?> getDonate(@RequestBody CommentRequestDto requestDto,
                                       HttpServletRequest request) {
        return commentService.getDonate(requestDto, request);
    }
}
