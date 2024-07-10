package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.service.UserBlogService;
import org.example.blogplatform.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/Ylog/{username}")
@RequiredArgsConstructor
public class UserBlogController {
    private final UserBlogService userBlogService;


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
    public String showUserBlog(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User loginUser = userBlogService.findByUsername(username);
            model.addAttribute("loginUser", loginUser);
        }


        return "YLogs/userBlog";
    }
}
