package com.kelompok7.mbglokal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kelompok7.mbglokal.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT nama_instansi FROM users WHERE id_user = :idUser", nativeQuery = true)
    String findNamaInstansiById(Long idUser);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET nama_instansi = :namaInstansi WHERE id_user = :idUser", nativeQuery = true)
    void updateNamaInstansi(Long idUser, String namaInstansi);
}