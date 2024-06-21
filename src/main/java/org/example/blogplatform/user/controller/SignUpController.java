package org.example.blogplatform.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.user.entity.User;
import org.example.blogplatform.user.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/signup")
@Slf4j
public class SignUpController {
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Autowired
    private SignUpService signUpService;

    @GetMapping
    public String showSignUpPage(Model model) {
        model.addAttribute("user", new User());
        return "YLog/signup";
    }

    @PostMapping
    public String signUp(
            @ModelAttribute User user,
            @RequestParam("profileImageFile") MultipartFile file,
            Model model) {
        boolean check = true;
        if (signUpService.checkUserInfoDuplication(user)){
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
            model.addAttribute("userRegMessage", "Welcome " + user.getUserName());
            signUpService.createUser(user);
        }
        return "YLog/userreg";
    }
}
