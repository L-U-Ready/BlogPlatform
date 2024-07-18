package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.PostStatus;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserBlogService;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/Ylog/{username}")
@RequiredArgsConstructor
@Slf4j
public class UserBlogController {
    private final UserBlogService userBlogService;
    private final PostService postService;


//    @GetMapping
//    public String showUserBlog(@CookieValue(value = "userId") String userId, Model model) {
//        Long loginId = Long.parseLong(userId);
//        User loginUser = userBlogService.findByUserId(loginId);
//        if (loginUser != null) {
//            model.addAttribute("loginUser", loginUser);
//        }
//        return "YLog/userBlog";
//    }

    @GetMapping
    public String showUserBlog(@PathVariable String username, Model model, Principal principal) throws UnsupportedEncodingException {
        log.info("principal 유저 :::" + principal.getName());
        log.info("사용자 이름 :::" + username);
        User loginUser = userBlogService.findByUsername(principal.getName());
        model.addAttribute("loginUser", loginUser);
        List<Post> posts = postService.findAllByUserOrderByReleaseDateDesc(loginUser);
        for(Post post : posts) {
            if(post.getPostStatus() == PostStatus.PUBLISHED) {
                model.addAttribute("posts", posts);
            }
        }

        User blogUser = userBlogService.findByUsername(username);
        model.addAttribute("blogUser", blogUser);
        return "YLogs/userBlog";
    }
}
