package com.example.trabalho_t2.controller;

import com.example.trabalho_t2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String roles) {
        try {
            userService.registerUser(username, password, roles);
            return "Usuário registrado com sucesso!";
        } catch (IllegalArgumentException e) {
            return "Erro: " + e.getMessage();
        }
    }
}