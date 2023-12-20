package com.example.plustest.service;


import com.example.plustest.dto.CommentRequstDto;
import com.example.plustest.dto.CommentResponseDto;
import com.example.plustest.dto.PostResponseDto;
import com.example.plustest.entity.Comment;
import com.example.plustest.entity.Post;
import com.example.plustest.entity.User;
import com.example.plustest.repository.CommentRepository;
import com.example.plustest.repository.PostRepository;
import com.example.plustest.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    // 댓글 작성 API
    public CommentResponseDto createComment(Long postId,
                                         CommentRequstDto commentRequestDto,
                                         User user) {
        // postId 가져오기
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));


        if (commentRequestDto.getText() == null) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        Comment comment = new Comment(commentRequestDto, user, post);
        post.addComment(comment);
        commentRepository.save(comment);
        return new CommentResponseDto (comment);

    }

    // 댓글 조회 API
    public List<CommentResponseDto> getComments(Long postId) {

        List<Comment> findCommentList = commentRepository.findAll();
        List<Comment> postCommentList = new ArrayList<>();

        for (Comment c : findCommentList) {
            if (c.getPost().getId().equals(postId)) {
                postCommentList.add(c);
            }
        }
        return postCommentList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // 댓글 수정 API
    @Transactional
    public CommentResponseDto modifyComment(Long commentId,
                                            CommentRequstDto commentRequestDto,
                                            UserDetailsImpl userDetails) throws AccessDeniedException {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (comment.getUser().getId().equals(userDetails.getUser().getId()) ) {
            comment.update(commentRequestDto);
            commentRepository.save(comment);
        } else {
            throw new AccessDeniedException("작성자만 댓글을 수정할 수 있습니다.");
        }
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId,
                                            UserDetailsImpl userDetails) throws AccessDeniedException {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        // 댓글 작성자와 유저id가 일치 또는 관리자 일 경우 삭제 가능
        if (comment.getUser().getId().equals(userDetails.getUser().getId())) {
            commentRepository.delete(comment);
        } else {
            throw new AccessDeniedException("작성자만 댓글을 삭제할 수 있습니다.");
        }
        return new CommentResponseDto(comment);
    }


}
