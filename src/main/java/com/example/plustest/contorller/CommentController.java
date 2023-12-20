package com.example.plustest.contorller;

import com.example.plustest.dto.CommentRequstDto;
import com.example.plustest.dto.CommentResponseDto;
import com.example.plustest.dto.CommonResponseDto;
import com.example.plustest.dto.PostResponseDto;
import com.example.plustest.security.UserDetailsImpl;
import com.example.plustest.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponseDto> createComment(@PathVariable Long postId,
                                                           @RequestBody CommentRequstDto commentRequstDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            commentService.createComment(postId, commentRequstDto, userDetails.getUser());
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new CommonResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return  ResponseEntity.ok().body(new CommonResponseDto("댓글 등록 완료", HttpStatus.OK .value()));
    }

    // 댓글 조회 API
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId){
        List<CommentResponseDto> commentResponseDto = commentService.getComments(postId);
        return ResponseEntity.status(201).body(commentResponseDto);
    }

    // 댓글 수정 API
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto> modifyComment( @PathVariable Long commentId,
                                                            @RequestBody CommentRequstDto commentRequstDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.modifyComment(commentId, commentRequstDto, userDetails);
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto("댓글 작성자만 수정 가능합니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok().body(new CommonResponseDto("댓글 수정 완료", HttpStatus.OK.value()));
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponseDto> deleteComment( @PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){

        try {
            commentService.deleteComment(commentId, userDetails);
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new CommonResponseDto("댓글 작성자만 삭제 가능합니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok().body(new CommonResponseDto("댓글 삭제 완료", HttpStatus.OK.value()));
    }
}
