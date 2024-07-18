//package org.example.blogplatform.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.blogplatform.domain.Comment;
//import org.example.blogplatform.domain.Post;
//import org.example.blogplatform.service.CommentService;
//import org.example.blogplatform.service.PostService;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.security.Principal;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class PostRestController {
//    private final PostService postService;
//    private final CommentService commentService;
//
//    @DeleteMapping("Ylog/{username}/post/{postId}/delete")
//    public void deletePost(@PathVariable String postId, @PathVariable String username) {
//        log.info("Delete post id " + postId);
//        postService.deletePostById(Long.valueOf(postId));
//    }
//    @PostMapping("/{postId}/{encodedTitle}/comment")
//    public String postComment(@PathVariable String postId, @ModelAttribute Comment comment,
//                              @PathVariable String username, Principal principal, @PathVariable String encodedTitle) {
//
//        Post post = postService.findById(Long.valueOf(postId));
//        comment.setPost(post);
//        comment.setAuthor(principal.getName());
//        log.info("comment : " + comment.getComment());
//        commentService.save(comment);
//
//        return "ok";
//
//    }
//}
