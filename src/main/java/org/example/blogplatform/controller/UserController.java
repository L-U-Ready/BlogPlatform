package org.example.blogplatform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.*;
import org.example.blogplatform.jwt.util.JwtTokenizer;
import org.example.blogplatform.security.dto.UserLoginDto;
import org.example.blogplatform.service.PostService;
import org.example.blogplatform.service.RefreshTokenService;
import org.example.blogplatform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Ylog")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;



    @GetMapping("/loginform")
    public String showLoginForm(Model model ) {
        model.addAttribute("loginDto", new UserLoginDto());
        return "YLogs/users/loginform";
    }


    @GetMapping("/signupform")
    public String signupform(){
        return "YLogs/users/signupform";
    }

    @PostMapping("/signup")
    public String signUp(
            @ModelAttribute User user,BindingResult result,
            Model model) throws IOException {
        if(result.hasErrors()) {
            model.addAttribute("errorMessage", "다시 입력해주세요.");
            return "error";
        }

        MultipartFile profileImageFile = user.getProfileImageFile();
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + profileImageFile.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/images/profiles/" + filename);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, profileImageFile.getBytes());
            user.setProfileImage(filename);
        } else {
            user.setProfileImage("default.png");
        }

        model.addAttribute("userRegMessage", "Welcome " + user.getUsername());
        userService.createUser(user, passwordEncoder);

        return "redirect:/Ylog";
    }

    @GetMapping("/{username}/settings")
    public String showSettings(@PathVariable String username, Model model, Principal principal) {
        if(username.equals(principal.getName())) {
            User user = userService.findByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        return "YLogs/users/setting";
    }

    @PutMapping("/{username}/settings/update")
    public String editUserInfo(@PathVariable String username, @ModelAttribute User updateUser, Principal principal) {
        if(username.equals(principal.getName())) {
            User user = userService.findByUsername(principal.getName());
            userService.updateUser(user, updateUser);
        }
        return "YLogs/users/setting";
    }

    @DeleteMapping("/{username}/settings/delete")
    public String deleteUser(@PathVariable String username, @RequestParam("password") String password){
        User user = userService.findByUsername(username);
        if(user.getPassword().equals(password)) {
            userService.deleteUser(user);
            log.info("User {} deleted", user.getUsername());
        }
        return "redirect:/logout";
    }

    @GetMapping("/{username}/drafts")
    public String showUserBlogDrafts(@PathVariable String username, Model model) {
        User loginUser = userService.findByUsername(username);
        model.addAttribute("loginUser", loginUser);
        List<Post> allPosts = postService.findAllByUserOrderByReleaseDateDesc(loginUser);
        List<Post> archivedPosts = new ArrayList<>();

        for (Post post : allPosts) {
            if ("ARCHIVED".equals(post.getPostStatus().toString())) {
                archivedPosts.add(post);
            }
        }
        model.addAttribute("posts", archivedPosts);
        return "YLogs/draft";
    }

}
