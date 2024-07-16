package org.example.blogplatform.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.RefreshToken;
import org.example.blogplatform.domain.Role;
import org.example.blogplatform.domain.SocialLoginInfo;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.dto.UserLoginResponseDto;
import org.example.blogplatform.jwt.util.CookieUtil;
import org.example.blogplatform.jwt.util.JwtTokenizer;
import org.example.blogplatform.security.dto.UserLoginDto;
import org.example.blogplatform.service.RefreshTokenService;
import org.example.blogplatform.service.SocialLoginInfoService;
import org.example.blogplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Ylog")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;



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

    @PutMapping("/{username}/settings")
    public String editUserInfo(@PathVariable String username, @ModelAttribute User updateUser, Principal principal) {
        if(username.equals(principal.getName())) {
            User user = userService.findByUsername(principal.getName());
            userService.updateUser(user, updateUser);
        }
        return "YLogs/users/setting";
    }

    @DeleteMapping("/{username}/delete")
    public void deleteUser(@PathVariable String username){
        User user = userService.findByUsername(username);
        userService.deleteUser(user);
    }


}
