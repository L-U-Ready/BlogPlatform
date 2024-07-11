package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.repository.PostRepository;
import org.example.blogplatform.repository.UserBlogRepository;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBlogService {

    private final UserBlogRepository userBlogRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public User findByUserId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Post> findPostsByUser(User user) {
        return postRepository.findByUser(user);
    }
}
