package org.example.blogplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/Ylog")
@RequiredArgsConstructor
@Slf4j
public class YlogController {
    private final PostService postService;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    @GetMapping
    public String Ylog(Model model, Principal principal) {
        List<Post> posts = postService.findAllByOrderByReleaseDateDesc();
        model.addAttribute("posts", posts);

        try {
            String postsJson = objectMapper.writeValueAsString(posts);
            model.addAttribute("postsJson", postsJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting posts to JSON", e);
        }

        if (principal != null) {
            User loginUser = userService.findByUsername(principal.getName());
            model.addAttribute("loginUser", loginUser);
        }

        return "YLogs/ylog";
    }

    @GetMapping("/sort")
    @ResponseBody
    public List<Post> sortPosts(@RequestParam String criteria) {
        switch (criteria) {
            case "views":
                return postService.findAllByOrderByViewsDesc();
            case "likes":
                return postService.findAllByOrderByLikesDesc();
            default:
                return postService.findAllByOrderByReleaseDateDesc();
        }
    }
}

