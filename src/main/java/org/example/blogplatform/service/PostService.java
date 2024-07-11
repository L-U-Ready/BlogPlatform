package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.PostRepository;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void savePost(String username, Post post) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        post.setUser(user);

        // username을 필요로 하는 추가 로직이 있다면 여기서 처리합니다.
        postRepository.save(post);
    }
}
