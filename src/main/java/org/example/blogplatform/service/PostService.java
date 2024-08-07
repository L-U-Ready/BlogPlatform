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
import java.util.Optional;

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
        post.setEncodedTitle(URLEncoder.encode(post.getTitle(), StandardCharsets.UTF_8));
        post.setAuthor(user.getUsername());
        log.info("인코드 제목 : " + post.getEncodedTitle());
        // username을 필요로 하는 추가 로직이 있다면 여기서 처리합니다.
        postRepository.save(post);
    }
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void updatePost(Post post, Post updatePost) {
        post.setTitle(updatePost.getTitle());
        post.setMent(updatePost.getMent());
        post.setContent(updatePost.getContent());
        postRepository.save(post);
    }

    public List<Post> findAllByOrderByReleaseDateDesc() {
        return postRepository.findAllByOrderByReleaseDateDesc();
    }

    public List<Post> findAllByOrderByViewsDesc() {
        return postRepository.findAllByOrderByViewsDesc();
    }

    public List<Post> findAllByOrderByLikesDesc() {
        return postRepository.findAllByOrderByLikesDesc();
    }


    public List<Post> findAllByUserOrderByReleaseDateDesc(User user) {
        return postRepository.findAllByUserOrderByReleaseDateDesc(user);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public void deletePostByTitle(String title) {
        postRepository.deletePostByTitle(title);
    }

}
