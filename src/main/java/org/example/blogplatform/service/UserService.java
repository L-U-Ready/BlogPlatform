package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;

import org.example.blogplatform.domain.Role;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.RoleRepository;
import org.example.blogplatform.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//
////    회원가입
//    @Transactional
//    public User registUser(User user){
//        //role 추가
//        Role userRole = roleRepository.findByName("USER");
//        user.setRoles(Collections.singleton(userRole));
//        //password 암호화해서 넣어줘야한다.
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        return  userRepository.save(user);
//    }

    @Transactional(readOnly = true)
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByProviderAndSocialId(String provider, String socialId){
        return userRepository.findByProviderAndSocialId(provider,socialId);
    }

    public boolean checkUserInfoDuplication(User user) {
        User checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser == null){
            return true;
        } else {
            return user.getEmail() != null && !user.getEmail().equals(checkUser.getEmail());
        }
    }

    @Transactional
    public void createUser(User user, PasswordEncoder passwordEncoder) {
        // userBlogName 설정하지 않으면 기본값 설정
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getUsername());
        }
        user.setSocialId(null);
        user.setProvider("YLog");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public User saveUser(String username, String name, String email, String socialId, String provider, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setSocialId(socialId);
        user.setProvider(provider);
        user.setPassword(passwordEncoder.encode("")); // 비밀번호는 소셜 로그인 사용자의 경우 비워둡니다.
        return userRepository.save(user);
    }


}