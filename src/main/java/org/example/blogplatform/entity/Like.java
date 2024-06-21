package org.example.blogplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.blogplatform.user.entity.User;

import javax.annotation.processing.Generated;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Like {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
