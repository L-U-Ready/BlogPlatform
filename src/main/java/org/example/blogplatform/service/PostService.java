package org.example.blogplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.repository.PostRepository;
import org.example.blogplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void savePost(String username, Post post) throws UnsupportedEncodingException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        post.setUser(user);
        post.setEncodedTitle(URLEncoder.encode(post.getTitle(), StandardCharsets.UTF_8.name()));
        log.info("인코드 제목 : " + post.getEncodedTitle());
        // username을 필요로 하는 추가 로직이 있다면 여기서 처리합니다.
        postRepository.save(post);
    }

    public Post findByTitle(String title) {
        return postRepository.findByTitle(title);
    }

    public void updatePost(Post post, Post updatePost) {
        post.setTitle(updatePost.getTitle());
        post.setMent(updatePost.getMent());
        post.setContent(updatePost.getContent());
        postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllByOrderByReleaseDateDesc() {
        return postRepository.findAllByOrderByReleaseDateDesc();
    }

}
