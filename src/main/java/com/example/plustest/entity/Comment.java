package com.example.plustest.entity;

import com.example.plustest.dto.CommentRequstDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CommentId;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(CommentRequstDto commentRequstDto, User user, Post post) {
        this.text = commentRequstDto.getText();
        this.user = user;
        this.post = post;
    }

    // 서비스 메서드
    public void setText(String text) {
        this.text = text;
    }

    public void update(CommentRequstDto commentRequstDto) {
        this.text = commentRequstDto.getText();
    }
}
