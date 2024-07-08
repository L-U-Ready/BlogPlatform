package org.example.blogplatform.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.SocialLoginInfo;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.service.SocialLoginInfoService;
import org.example.blogplatform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Ylog")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @GetMapping
    public String YLog() {
        return "YLogs/ylog";
    }

    @GetMapping("/loginform")
    public String showLoginForm(Model model ) {
        model.addAttribute("user", new User());
        return "Ylogs/users/loginform";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") User user, Model model) {
//        User foundUser = userService.findByUsername(user.getUsername());
//        if (foundUser != null) {
//            model.addAttribute("user", foundUser);
//            return "redirect:/Ylog/" + foundUser.getUsername();
//        } else {
//            return "redirect:/Ylog/loginform";
//        }
//    }

    @GetMapping("/signupform")
    public String signupform(){
        return "YLogs/users/signupform";
    }

    @PostMapping("/signup")
    public String signUp(
            @ModelAttribute User user,
            @RequestParam("profileImageFile") MultipartFile file,
            Model model) {
        boolean check = true;
        if (userService.checkUserInfoDuplication(user)){
            model.addAttribute("emailCheckRes", "Email is Unique");
        } else {
            model.addAttribute("emailCheckRes", "Email is Duplicated");
            check = false;
        }
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setProfile_image(filePath.toString());
            } else {
                user.setProfile_image(UPLOAD_DIR + "default.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
            check = false;
        } catch (Exception e) {
            model.addAttribute("userRegMessage", "You Failed SignUp \n " + e);
            check = false;
        }

        if(check){
            model.addAttribute("userRegMessage", "Welcome " + user.getUsername());
            userService.createUser(user, passwordEncoder);
        }
        return "redirect:/Ylog";
    }
}
