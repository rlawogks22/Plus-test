package com.example.plustest.service;

import com.example.plustest.dto.CommonResponseDto;
import com.example.plustest.dto.PostRequestDto;
import com.example.plustest.dto.PostResponseDto;
import com.example.plustest.entity.Post;
import com.example.plustest.entity.User;
import com.example.plustest.repository.PostRepository;
import com.example.plustest.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private User user;

    public void createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {

        // 예외 처리
        if (postRequestDto.getTitle() == null && postRequestDto.getTitle().length() > 500) {
            throw new IllegalArgumentException("제목이 입력되지 않았거나 제목을 500자 이하로 입력하세요");
        } else if (postRequestDto.getContent() == null && postRequestDto.getContent().length() > 5000) {
            throw new IllegalArgumentException("내용이 입력되지 않았거나 내용을 5000자 이하로 입력하세요.");
        }

        // 받아온 정보로 post 객체 생성
        Post post = new Post(postRequestDto, userDetails);

        // DB에 저장
        postRepository.save(post);
    }

    public List<PostResponseDto> getPostList() {
        // post 전체 리스트 생성
        List<Post> postList = postRepository.findAll();
        // 반환 타입의 리스트 생성
        List<PostResponseDto> responseDtoList = new ArrayList<>();

        // 반복문을 통해 postList의 내용물을 반환 타입의 리스트에 담은 후 반환
        for (Post post : postList) {
            responseDtoList.add(new PostResponseDto(post));
        }
        return responseDtoList;
    }

    public PostResponseDto getPost(Long postId) {
        // 해당 게시물의 id와 일치하는지 검증 및 post 객체 생성
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 id의 게시물이 없습니다."));
        // DTO로 변환 후 반환
        return new PostResponseDto(post);
    }


    public void updatePost(Long postId, PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        // 권한 검증 및 post 객체 생성
        Post post = checkPostIdAndUser(postId, userDetails);
        // 받아온 정보로 게시글 수정
        post.update(postRequestDto);

        postRepository.save(post);
    }
    @Transactional
    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        // 권한 검증 및 post 객체 생성
        Post post = checkPostIdAndUser(postId, userDetails);
        // DB에서 삭제
        postRepository.delete(post);
    }


    private Post checkPostIdAndUser(Long postId, UserDetailsImpl userDetails) {
        // 해당 id의 게시물이 존재하는지 검증 및 post 객체 생성
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 id의 게시물이 없습니다."));

        if (!Objects.equals(post.getUser().getId(), userDetails.getUser().getId())){
            throw new IllegalArgumentException("게시물 작성자만 수정 및 삭제 가능합니다.");
        }

        return post;
    }

}
