package org.example.blogplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.blogplatform.user.entity.User;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String comment;

}
