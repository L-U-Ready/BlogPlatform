package org.example.blogplatform.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blogplatform.user.entity.User;
import org.example.blogplatform.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping
    public String showLoginForm(Model model ) {
        model.addAttribute("user", new User());
        return "YLog/login";
    }
    @PostMapping
    public String login(@ModelAttribute("user") User user, Model model, HttpServletResponse response) {
        User foundUser = loginService.getUserByEmail(user.getEmail());
        if (loginService.vaildateUser(foundUser.getEmail(), foundUser.getPassword())) {
            model.addAttribute("user", foundUser);
            Cookie cookie = new Cookie("userId", foundUser.getId().toString());
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/" + foundUser.getUserName();
        }
        else {
//            model.addAttribute("error", "Invaild email or password");
            return "redirect:/login";
        }
    }

}
