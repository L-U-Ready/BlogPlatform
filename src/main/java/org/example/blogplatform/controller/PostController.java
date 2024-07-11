package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/Ylog/{username}/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/posts/thumbnails/";


    @GetMapping
    public String showUserBlog(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
//        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());  // 추가된 부분
        return "YLogs/post";
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
}
