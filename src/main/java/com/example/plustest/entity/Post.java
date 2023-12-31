package com.example.plustest.entity;

import com.example.plustest.dto.PostRequestDto;
import com.example.plustest.security.UserDetailsImpl;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> commentList;

    public Post(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.user = userDetails.getUser();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void addComment(Comment comment){
        this.commentList.add(comment);
    }

}
