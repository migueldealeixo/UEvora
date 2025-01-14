package com.example.trabalho_t2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.trabalho_t2.Repository.UserRepo;
import com.example.trabalho_t2.entitys.Users;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public Users registerUser(String username,String password, String role){
        if(userRepo.findByUsername(username)!= null){
            throw new IllegalArgumentException("Utilizador já existe...");
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepo.save(user);



    }
}
