package com.example.together.controller;

import com.example.together.controller.request.CheerRequestDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.service.CheerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CheerController {
    private final CheerService cheerService;

    /* 응원하기 전체조회 */
    @GetMapping("/cheer/{postId}")
    public ResponseDto<?> cheerTotal(@PathVariable Long postId, HttpServletRequest request){
        return cheerService.cheerTotal(postId, request);
    }

    /* 응원하기 클릭 */
    @PostMapping("/cheer/{postId}")
    public ResponseDto<?> cheerClick(@PathVariable Long postId, @RequestBody CheerRequestDto cheerRequestDto, HttpServletRequest request) {
        return cheerService.cheerClick(postId, cheerRequestDto, request);
    }
}
