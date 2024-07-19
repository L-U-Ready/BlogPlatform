package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Comment;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.CommentService;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @DeleteMapping("/Ylog/comments/{commentId}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/Ylog/posts/{postId}/comments")
    public ResponseEntity<Void> postComment(
            @PathVariable Long postId,
            @RequestParam(required = false) Long parentId,
            @RequestBody Comment comment,
            Principal principal) {
        commentService.saveComment(postId, parentId, comment, principal.getName());
        return ResponseEntity.ok().build();
    }

    // 대댓글 작성을 위한 엔드포인트
    @PostMapping("/Ylog/posts/{postId}/comments/reply")
    public ResponseEntity<Void> postReply(@PathVariable Long postId, @RequestParam Long parentId, @RequestBody Comment comment, Principal principal) {
        Post post = postService.findById(postId);
        Comment parentComment = commentService.findById(parentId);
        comment.setParent(parentComment);
        comment.setPost(post);
        User commentUser = userService.findByUsername(principal.getName());
        comment.setUser(commentUser);
        comment.setAuthor(commentUser.getUsername());
        commentService.save(comment);
        return ResponseEntity.ok().build();
    }
}
