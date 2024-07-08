package org.example.blogplatform.service;

import org.example.blogplatform.repository.UserBlogRepository;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBlogService {

    private final UserBlogRepository userBlogRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserBlogService(UserBlogRepository userBlogRepository, UserRepository userRepository) {
        this.userBlogRepository = userBlogRepository;
        this.userRepository = userRepository;
    }

    public User findByUserId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
