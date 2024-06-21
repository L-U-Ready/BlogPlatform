package org.example.blogplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.blogplatform.user.entity.User;

@Entity
@Table(name = "userBlogs")
@Getter
@Setter
public class UserBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
