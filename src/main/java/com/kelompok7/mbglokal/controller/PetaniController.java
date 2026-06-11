package com.kelompok7.mbglokal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.entity.Petani;
import com.kelompok7.mbglokal.service.PetaniService;

@RestController
@RequestMapping("/api/petani")
public class PetaniController {

    private final PetaniService petaniService;

    public PetaniController(PetaniService petaniService) {
        this.petaniService = petaniService;
    }

    @GetMapping
    public List<Petani> getAllPetani() {
        return petaniService.getAllPetani();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Petani> getPetaniById(@PathVariable Long id) {
        Optional<Petani> petani = petaniService.getPetaniById(id);
        return petani.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Petani createPetani(@RequestBody Petani petani) {
        return petaniService.createPetani(petani);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Petani> updatePetani(
            @PathVariable Long id,
            @RequestBody Petani petani) {

        Optional<Petani> existing = petaniService.getPetaniById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Petani data = existing.get();
        data.setNamaPetani(petani.getNamaPetani());
        data.setAlamat(petani.getAlamat());
        data.setKontak(petani.getKontak());

        return ResponseEntity.ok(petaniService.createPetani(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetani(@PathVariable Long id) {
        petaniService.deletePetani(id);
        return ResponseEntity.ok().build();
    }
}