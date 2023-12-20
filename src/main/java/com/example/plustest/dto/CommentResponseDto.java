package com.example.plustest.dto;


import com.example.plustest.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto extends CommonResponseDto {

    private Long commentId;
    private String nickname;
    private String text;
//    private LocalDateTime createAt;
//    private LocalDateTime modifiedAt;
    //UTC 시간
    private Date createdAt;
    private Date modifiedAt;
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        // user구현 시 닉네임 추가
        this.nickname = comment.getUser().getNickname();
        this.text = comment.getText();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
