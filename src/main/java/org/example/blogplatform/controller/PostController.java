package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Comment;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.CommentService;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/Ylog/{username}/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/posts/thumbnails/";
    private static final int THUMBNAIL_WIDTH = 300; // 원하는 너비
    private static final int THUMBNAIL_HEIGHT = 300; // 원하는 높이

    @GetMapping
    public String showPost(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
//        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());  // 추가된 부분
        return "YLogs/posts/post";
    }

    @PostMapping
    public String postContent(@PathVariable String username, @ModelAttribute Post post) throws IOException {

        MultipartFile thumbnailImage = post.getThumbnailImageFile();
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + thumbnailImage.getOriginalFilename();
            Path imagePath = Paths.get(UPLOAD_DIR + filename);
            Files.createDirectories(imagePath.getParent());

            Files.write(imagePath, thumbnailImage.getBytes());
            post.setThumbnailImage(filename);
        } else {
            post.setThumbnailImage("default.png");
        }
        log.info("포스트 정보 ::: " + post);
        postService.savePost(username, post);
        return "redirect:/Ylog/" + username;
    }

    @GetMapping("/{postId}/{encodedTitle}")
    public String showPostDetail(@PathVariable String username,  Model model, @PathVariable Long postId){
        User loginUser = userService.findByUsername(username);
        Post post = postService.findById(postId);
        List<Comment> allComments = commentService.findAllByPostIdOrderByCreationDate(postId);

        model.addAttribute("loginUser", loginUser);
        model.addAttribute("post", post);
        model.addAttribute("comments", allComments);
        model.addAttribute("username", username);

        return "YLogs/posts/postDetail";
    }


    @GetMapping("/{postId}/{encodedTitle}/edit")
    public String showEditPostForm(@PathVariable String username, @PathVariable String encodedTitle, Model model, @PathVariable String postId) {
        Post post = postService.findById(Long.valueOf(postId));
        model.addAttribute("post", post);
        model.addAttribute("username", username);
        return "YLogs/posts/postEdit";
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@PathVariable String username, @ModelAttribute Post updatePost, @PathVariable String postId) {
        Post post = postService.findById(Long.valueOf(postId));
        postService.updatePost(post, updatePost);
        return "redirect:/Ylog/" + username;
    }

    @DeleteMapping("/{postId}/delete")
    public void deletePost(@PathVariable String postId) {
        log.info("Delete post id " + postId);
        postService.deletePostById(Long.valueOf(postId));
    }


}
