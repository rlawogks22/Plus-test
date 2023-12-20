package com.example.plustest.dto;

import com.example.plustest.entity.Comment;
import com.example.plustest.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostListResponseDto extends CommonResponseDto{
    private String title;
    private String nickname;
    private String content;
    //UTC 시간
    private Date createdAt;
    private Date modifiedAt;
    private List<Comment> commentList;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = post.getCommentList();
    }

}
