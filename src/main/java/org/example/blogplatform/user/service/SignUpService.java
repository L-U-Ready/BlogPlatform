package org.example.blogplatform.user.service;

import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.user.entity.User;
import org.example.blogplatform.user.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class SignUpService {
    @Autowired
    private SignUpRepository signUpRepository;


    public void createUser(User user) {
        // userBlogName 설정하지 않으면 기본값 설정
        if (user.getBlogName() == null || user.getBlogName().isEmpty()) {
            user.setBlogName(user.getUserName());
        }
        signUpRepository.save(user);
    }
    public boolean checkUserInfoDuplication(User user) {
        User checkUser = signUpRepository.findByEmail(user.getEmail());
        if (checkUser == null){
            return true;
        } else {
            return user.getEmail() != null && !user.getEmail().equals(checkUser.getEmail());
        }
    }
}
