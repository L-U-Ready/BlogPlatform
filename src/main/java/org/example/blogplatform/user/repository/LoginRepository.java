package org.example.blogplatform.user.repository;

import org.example.blogplatform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
