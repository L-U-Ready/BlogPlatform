package org.example.blogplatform.user.service;

import org.example.blogplatform.user.entity.User;
import org.example.blogplatform.user.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public User getUserByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    public boolean vaildateUser(String email, String password) {
        User user = getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

}
