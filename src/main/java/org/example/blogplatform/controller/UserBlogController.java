package org.example.blogplatform.controller;

import org.example.blogplatform.service.UserBlogService;
import org.example.blogplatform.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Ylog/{username}")
public class UserBlogController {
    @Autowired
    private UserBlogService userBlogService;


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
    public String showUserBlog() {
        return "YLogs/userBlog";
    }
}
