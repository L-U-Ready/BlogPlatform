package org.example.blogplatform.controller;

import org.example.blogplatform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{userName}/post")
public class PostController {
    @Autowired
    private PostService postService;


}
