package org.example.blogplatform.service;

import org.example.blogplatform.entity.UserBlog;
import org.example.blogplatform.repository.UserBlogRepository;
import org.springframework.stereotype.Service;

@Service
public class UserBlogService {
    private UserBlogRepository userBlogRepository;

    public UserBlog findByStringUserId(String userId) {
        return userBlogRepository.findByStringUserId(userId);
    }
}
