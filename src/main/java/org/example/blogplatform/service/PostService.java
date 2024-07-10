package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.domain.UserBlog;
import org.example.blogplatform.repository.PostRepository;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void savePost(String username, String content) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        postRepository.save(post);
    }
}
