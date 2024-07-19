package org.example.blogplatform.repository;

import org.example.blogplatform.domain.Post;
import org.example.blogplatform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);

    Post findByTitle(String title);
    Post findByIdAndTitle(Long id, String title);
    List<Post> findAllByOrderByReleaseDateDesc();
    List<Post> findAllByOrderByViewsDesc();
    List<Post> findAllByOrderByLikesDesc();

    List<Post> findAllByUserOrderByReleaseDateDesc(User user);

    void deletePostByTitle(String title);
}
