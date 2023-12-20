package com.example.plustest.repository;

import com.example.plustest.entity.Comment;
import com.example.plustest.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}

