package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Comment;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> findAllByPostIdOrderByCreationDate(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreationDate(postId);
    }

    public void saveComment(Comment comment) {
//        comment.setAuthor(username);
//        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findByParent(Comment parent) {
        return commentRepository.findByParent(parent);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
