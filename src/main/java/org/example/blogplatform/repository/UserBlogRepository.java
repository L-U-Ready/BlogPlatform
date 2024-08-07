package org.example.blogplatform.repository;

import org.example.blogplatform.domain.UserBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlogRepository extends JpaRepository<UserBlog, Long> {
}
