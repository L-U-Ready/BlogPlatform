package org.example.blogplatform.user.service;

import org.example.blogplatform.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 사용자 정보 세팅 서비스 구현

}
