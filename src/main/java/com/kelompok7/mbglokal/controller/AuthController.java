package com.kelompok7.mbglokal.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.dto.LoginRequest;
import com.kelompok7.mbglokal.entity.PenerimaManfaat;
import com.kelompok7.mbglokal.entity.Petani;
import com.kelompok7.mbglokal.entity.User;
import com.kelompok7.mbglokal.repository.PenerimaManfaatRepository;
import com.kelompok7.mbglokal.repository.PetaniRepository;
import com.kelompok7.mbglokal.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PenerimaManfaatRepository penerimaManfaatRepository;
    private final PetaniRepository petaniRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Login berhasil",
                        "username", user.getUsername(),
                        "role", user.getRole(),
                        "idUser", user.getIdUser()));
            } else {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Password salah"));
            }
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Username tidak ditemukan"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (user.getUsername() == null ||
                user.getPassword() == null ||
                user.getRole() == null) {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", "Data tidak lengkap"));
        }

        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", "Username sudah digunakan"));
        }

        User saved;

        if ("SEKOLAH".equals(user.getRole()) || "PENERIMA".equals(user.getRole())) {
            PenerimaManfaat pm = new PenerimaManfaat();
            pm.setUsername(user.getUsername());
            pm.setPassword(user.getPassword());
            pm.setRole("SEKOLAH");
            pm.setNamaInstansi(user.getNamaInstansi());
            pm.setKontak(user.getKontak());
            pm.setAlamat(user.getAlamat());
            saved = penerimaManfaatRepository.save(pm);

            // Fix: paksa update nama_instansi di tabel users lewat native query
            if (user.getNamaInstansi() != null) {
                userRepository.updateNamaInstansi(saved.getIdUser(), user.getNamaInstansi());
            }

        } else if ("PETANI".equals(user.getRole())) {
            Petani petani = new Petani();
            petani.setUsername(user.getUsername());
            petani.setPassword(user.getPassword());
            petani.setRole("PETANI");
            petani.setNamaKelompok(user.getNamaPetani());
            petani.setKontak(user.getKontak());
            petani.setAlamat(user.getAlamat());
            saved = petaniRepository.save(petani);

        } else {
            saved = userRepository.save(user);
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Register berhasil",
                "idUser", saved.getIdUser()));
    }
}