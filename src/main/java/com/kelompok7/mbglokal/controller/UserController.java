package com.kelompok7.mbglokal.controller;

import com.kelompok7.mbglokal.entity.User;
import com.kelompok7.mbglokal.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

        User user = userRepository.findById(id).orElseThrow();

        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());
        user.setNamaInstansi(updatedUser.getNamaInstansi());
        user.setNamaPetani(updatedUser.getNamaPetani());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(updatedUser.getPassword());
        }

        return userRepository.save(user);
    }
}