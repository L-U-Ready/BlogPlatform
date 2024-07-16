package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/Ylog")
@RequiredArgsConstructor
@Slf4j
public class YlogController {
    private final PostService postService;
    @GetMapping
    public String Ylog(Model model) {
        List<Post> posts = postService.findAllByOrderByReleaseDateDesc();
        model.addAttribute("posts", posts);
        return "YLogs/ylog";
    }
    
}
