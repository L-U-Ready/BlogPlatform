package org.example.blogplatform.controller;

import org.example.blogplatform.entity.UserBlog;
import org.example.blogplatform.service.UserBlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/{userName}")
public class UserBlogController {
    private UserBlogService userBlogService;


    @GetMapping
    public String showUserBlog(@CookieValue(value = "userId", defaultValue = "") String userId, Model model) {
        UserBlog userBlog = userBlogService.findByStringUserId(userId);
        model.addAttribute("userBlog", userBlog);
        return "YLog/userBlog";
    }

}
