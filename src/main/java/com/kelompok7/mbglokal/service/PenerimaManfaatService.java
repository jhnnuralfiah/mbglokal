package com.kelompok7.mbglokal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kelompok7.mbglokal.entity.PenerimaManfaat;
import com.kelompok7.mbglokal.repository.PenerimaManfaatRepository;

@Service
public class PenerimaManfaatService {

    private final PenerimaManfaatRepository penerimaManfaatRepository;

    public PenerimaManfaatService(PenerimaManfaatRepository penerimaManfaatRepository) {
        this.penerimaManfaatRepository = penerimaManfaatRepository;
    }

    public List<PenerimaManfaat> getAllPenerimaManfaat() {
        return penerimaManfaatRepository.findAll();
    }

    public Optional<PenerimaManfaat> getPenerimaManfaatById(Long id) {
        return penerimaManfaatRepository.findById(id);
    }

    public PenerimaManfaat createPenerimaManfaat(PenerimaManfaat penerimaManfaat) {
        return penerimaManfaatRepository.save(penerimaManfaat);
    }

    public void deletePenerimaManfaat(Long id) {
        penerimaManfaatRepository.deleteById(id);
    }
    public PenerimaManfaat updatePenerimaManfaat(
        Long id,
        PenerimaManfaat data
) {

    PenerimaManfaat existing =
            penerimaManfaatRepository
            .findById(id)
            .orElseThrow();

    existing.setUsername(
            data.getUsername()
    );

    existing.setPassword(
            data.getPassword()
    );

    existing.setRole(
            data.getRole()
    );

    existing.setNamaInstansi(
            data.getNamaInstansi()
    );

    existing.setNamaPetani(
            data.getNamaPetani()
    );

    return penerimaManfaatRepository
            .save(existing);
}
}