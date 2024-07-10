package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Ylog/{username}/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping
    public String showUserBlog(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
//        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());  // 추가된 부분
        return "YLogs/post";
    }

    @PostMapping
    public String postContent(@PathVariable String username, @ModelAttribute Post post) {
        String content = post.getContent();
        String ment = post.getMent();
        String title = post.getTitle();

        // content를 사용하여 필요한 로직 수행
        // 예: 데이터베이스에 저장
        postService.savePost(username, content, ment, title);

        // 필요에 따라 다른 페이지로 리다이렉트
        return "redirect:/Ylog/" + username;
    }
}
