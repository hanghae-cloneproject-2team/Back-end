package com.example.together.service;

import com.example.together.controller.response.ResponseDto;
import com.example.together.controller.response.CommentResponseDto;
import com.example.together.domain.Comment;
import com.example.together.domain.DonationType;
import com.example.together.domain.Member;
import com.example.together.domain.Post;
import com.example.together.controller.request.CommentRequestDto;
import com.example.together.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommonService commonService;
    
    // 기부타입 ( c: 댓글기부 / D: 직접기부 )
    private DonationType typeComment = DonationType.C; //
    private DonationType typeDonation = DonationType.D; //

    /* 댓글 작성 */
    @Transactional
    public ResponseDto<?> writeComment(CommentRequestDto requestDto, HttpServletRequest request) {
        // 1. 로그인 확인
        commonService.loginCheck(request);

        // 2. Token validation => member 생성
        Member member = commonService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        // 3. 게시글 존재여부 확인
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        post.addPriceState(100L);
        // 4. insert 정보
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .comment(requestDto.getComment())
                .nickname(member.getNickname())
                .donation(requestDto.getDonation())
                .donationType(typeComment)
                .build();
        commentRepository.save(comment);

        // 답글을 추가하면 +100원이 됨
        return ResponseDto.success(" + 100원! ");
    }

    /* 직접 기부하기 */
    @Transactional
    public ResponseDto<?> getDonate(CommentRequestDto requestDto, HttpServletRequest request) {
        // 1. 로그인 확인
        commonService.loginCheck(request);

        // 2. Token validation => member 생성
        Member member = commonService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        // 3. 게시글 존재여부 확인
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        post.addPriceState(requestDto.getDonation());
        // 4. insert 정보
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .comment(requestDto.getComment())
                .nickname(member.getNickname())
                .donation(requestDto.getDonation())
                .donationType(typeDonation)
                .build();
        commentRepository.save(comment);

        // 답글을 추가하면 +100원이 됨
        return ResponseDto.success(" 기부에 동참해주셔서 감사합니다 !");
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId) {
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .nickname(comment.getMember().getNickname())
                            .comment(comment.getComment())
                            .donation(comment.getDonation())
                            .createdAt(comment.getCreatedAt())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
    }

    /* 댓글 삭제 */
    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        // 1. 로그인 확인
        commonService.loginCheck(request);

        // 2. Token validation => member 생성
        Member member = commonService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        
        // 3. 댓글 존재여부 확인
        Comment comment = isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글입니다.");
        }
        // 4. 작성자 확인
        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        // 5. 삭제
        if(comment.getDonationType().equals("C")) {
            commentRepository.delete(comment);
            return ResponseDto.success("삭제");
        } else {
            comment.update(requestDto);
            return ResponseDto.success("success");
        }
    }

    /* 댓글 존재여부 확인 */
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }

}
