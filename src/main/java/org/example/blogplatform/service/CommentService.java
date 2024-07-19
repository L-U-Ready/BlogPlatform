package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Comment;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.CommentRepository;
import org.example.blogplatform.repository.PostRepository;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> findAllByPostIdOrderByCreationDate(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreationDate(postId);
    }

    @Transactional
    public void saveComment(Long postId, Long parentId, Comment comment, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        User user = userRepository.findByUsername(username);

        comment.setPost(post);
        comment.setUser(user);
        comment.setAuthor(user.getUsername());

        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
            comment.setParent(parentComment);
        }

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
