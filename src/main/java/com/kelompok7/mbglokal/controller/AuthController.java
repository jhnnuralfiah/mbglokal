package com.kelompok7.mbglokal.controller;

import com.kelompok7.mbglokal.entity.User;
import com.kelompok7.mbglokal.repository.UserRepository;
import com.kelompok7.mbglokal.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {

            User user = userOpt.get();

            if (user.getPassword().equals(request.getPassword())) {

                return ResponseEntity.ok(
                        java.util.Map.of(
                                "success", true,
                                "message", "Login berhasil",
                                "username", user.getUsername(),
                                "role", user.getRole(),
                                "idUser", user.getIdUser()));

            } else {

                return ResponseEntity.status(401).body(
                        java.util.Map.of(
                                "success", false,
                                "message", "Password salah"));
            }

        } else {

            return ResponseEntity.status(404).body(
                    java.util.Map.of(
                            "success", false,
                            "message", "Username tidak ditemukan"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // cek username sudah ada
        Optional<User> existing = userRepository.findByUsername(user.getUsername());

        if (existing.isPresent()) {
            return ResponseEntity.status(400).body(
                    java.util.Map.of(
                            "success", false,
                            "message", "Username sudah digunakan"));
        }

        // validasi minimal
        if (user.getUsername() == null ||
                user.getPassword() == null ||
                user.getRole() == null) {

            return ResponseEntity.status(400).body(
                    java.util.Map.of(
                            "success", false,
                            "message", "Data tidak lengkap"));
        }

        User saved = userRepository.save(user);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "success", true,
                        "message", "Register berhasil",
                        "idUser", saved.getIdUser()));
    }
}