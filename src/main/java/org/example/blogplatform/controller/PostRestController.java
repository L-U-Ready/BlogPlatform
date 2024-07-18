//package org.example.blogplatform.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.blogplatform.service.PostService;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//public class PostRestController {
//    private final PostService postService;
//
//    @DeleteMapping("Ylog/{username}/post/{postId}/delete")
//    public void deletePost(@PathVariable String postId, @PathVariable String username) {
//        log.info("Delete post id " + postId);
//        postService.deletePostById(Long.valueOf(postId));
//    }
//
//}
