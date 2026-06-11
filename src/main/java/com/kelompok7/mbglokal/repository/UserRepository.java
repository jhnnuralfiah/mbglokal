package com.kelompok7.mbglokal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelompok7.mbglokal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Fungsi khusus untuk mencari User berdasarkan username-nya
    Optional<User> findByUsername(String username);
}