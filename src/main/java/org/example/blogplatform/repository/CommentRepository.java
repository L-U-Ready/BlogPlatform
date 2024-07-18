package org.example.blogplatform.repository;

import org.example.blogplatform.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreationDate(Long postId);

    List<Comment>findByParent(Comment parent);
}
