package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Comment;
import org.example.blogplatform.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @DeleteMapping("/Ylog/comment/{commentId}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/Ylog/reply/{replyId}/delete")
    public ResponseEntity<Void> deleteReply(@PathVariable Long replyId) {
        commentService.deleteComment(replyId);
        return ResponseEntity.ok().build();
    }
}
