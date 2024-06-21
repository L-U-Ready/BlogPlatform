package org.example.blogplatform.user.repository;

import org.example.blogplatform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
