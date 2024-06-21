package org.example.blogplatform.repository;

import org.example.blogplatform.entity.UserBlog;
import org.example.blogplatform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlogRepository extends JpaRepository<UserBlog, Long> {
    UserBlog findByStringUserId(String userId);

}
