package com.kelompok7.mbglokal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kelompok7.mbglokal.entity.PaketMenu;
import com.kelompok7.mbglokal.repository.PaketMenuRepository;

@Service
public class PaketMenuService {

    private final PaketMenuRepository paketMenuRepository;

    public PaketMenuService(PaketMenuRepository paketMenuRepository) {
        this.paketMenuRepository = paketMenuRepository;
    }

    public List<PaketMenu> getAllPaketMenu() {
        return paketMenuRepository.findAll();
    }

    public Optional<PaketMenu> getPaketMenuById(Long id) {
        return paketMenuRepository.findById(id);
    }

    public PaketMenu createPaketMenu(PaketMenu paketMenu) {
        return paketMenuRepository.save(paketMenu);
    }

    public void deletePaketMenu(Long id) {
        paketMenuRepository.deleteById(id);
    }
}
