package com.example.trabalho_t2.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trabalho_t2.entitys.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
    
}
