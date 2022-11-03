package com.example.together.service;

import com.example.together.controller.request.CheerRequestDto;
import com.example.together.controller.response.CheerResponseDto;
import com.example.together.controller.response.ResponseDto;
import com.example.together.domain.Cheer;
import com.example.together.domain.Member;
import com.example.together.domain.Post;
import com.example.together.repository.CheerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheerService  {
    private final CommonService commonService;
    private final PostService postService;
    private final CheerRepository cheerRepository;

    /* 응원하기 전체조회 */
    public ResponseDto<?> cheerTotal(Long postId, HttpServletRequest request) {
        // 1. 로그인 확인
        commonService.loginCheck(request);

        // 2. Token validation => member 생성
        Member member = commonService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 3. 게시글 존재여부 확인
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        
        // 해당 게시글에 응원하기를 클릭했는지 여부 ( Y: 응원하기 이미 참여 / N: 응원하기 아직 미참여)
        boolean cheerYn = false;
        // 4-1. 회원번호, 게시글번호 조회
        Optional<Cheer> cheerInfo = cheerRepository.findByMemberAndPost(member, post);
        // 4-2. 해당 게시글의 응원하기 전체갯수 조회
        Long cheerTotalCnt = cheerRepository.countByPostId(postId);

        // 5. 해당게시글의 응원하기 여부
        if(cheerInfo.isPresent()) {
            cheerYn = true;
        }
        CheerResponseDto cheerAllList = CheerResponseDto.builder()
                .CheerYn(cheerYn)
                .cheerTotalCnt(cheerTotalCnt)
                .build();

        return ResponseDto.success(cheerAllList);
    }


    /* 응원하기 클릭 */
    @Transactional
    public ResponseDto<?> cheerClick(Long postId, CheerRequestDto requestDto, HttpServletRequest request) {
        // 1. 로그인 확인
        commonService.loginCheck(request);

        // 2. Token validation => member 생성
        Member member = commonService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 3. 게시글 존재여부 확인
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        post.addPriceState(100L);
        // 4. 회원번호, 게시글번호 조회
        Optional<Cheer> cheerInfo = cheerRepository.findByMemberAndPost(member, post);

        // 5-1. 응원하기 기록이 있으면 msg출력
        // ** 한 번 응원하기를 누르면 취소 X
        if (cheerInfo.isPresent()) {
            return ResponseDto.fail("ALREADY_APPLIED", "이미 응모한 모금함이에요.");
        } else {

            // 5-2. 응원하기 기록이 없으면 저장
            Cheer cheer = Cheer.builder()
                    .member(member)
                    .post(post)
                    .donation(requestDto.getDonation())
                    .build();
            cheerRepository.save(cheer);

            return ResponseDto.success(" + 100원! ");
        }
    }
}
