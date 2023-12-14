package com.example.plustest.dto;

import com.example.plustest.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class PostResponseDto extends CommonResponseDto {
    private String title;
    private String nickname;
    private String content;
    //UTC 시간
    private Date createdAt;
    private Date modifiedAt;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}