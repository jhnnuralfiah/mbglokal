package com.kelompok7.mbglokal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kelompok7.mbglokal.entity.Petani;
import com.kelompok7.mbglokal.repository.PetaniRepository;

@Service
public class PetaniService {

    private final PetaniRepository petaniRepository;

    public PetaniService(PetaniRepository petaniRepository) {
        this.petaniRepository = petaniRepository;
    }

    // Fungsi GET (Melihat semua petani)
    public List<Petani> getAllPetani() {
        return petaniRepository.findAll();
    }

    // Fungsi GET by ID (Mencari 1 petani)
    public Optional<Petani> getPetaniById(Long id) {
        return petaniRepository.findById(id);
    }

    // Fungsi POST (Menambah petani baru)
    public Petani createPetani(Petani petani) {
        return petaniRepository.save(petani);
    }

    // Fungsi DELETE (Menghapus petani)
    public void deletePetani(Long id) {
        petaniRepository.deleteById(id);
    }
}