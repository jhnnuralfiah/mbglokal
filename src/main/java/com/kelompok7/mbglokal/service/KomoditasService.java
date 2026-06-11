package com.kelompok7.mbglokal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kelompok7.mbglokal.entity.Komoditas;
import com.kelompok7.mbglokal.repository.KomoditasRepository;

@Service
public class KomoditasService {

    private final KomoditasRepository komoditasRepository;

    public KomoditasService(KomoditasRepository komoditasRepository) {
        this.komoditasRepository = komoditasRepository;
    }

    public List<Komoditas> getAllKomoditas() {
        return komoditasRepository.findAll();
    }

    public Optional<Komoditas> getKomoditasById(Long id) {
        return komoditasRepository.findById(id);
    }

    public Komoditas createKomoditas(Komoditas komoditas) {
        return komoditasRepository.save(komoditas);
    }

    public void deleteKomoditas(Long id) {
        komoditasRepository.deleteById(id);
    }
}