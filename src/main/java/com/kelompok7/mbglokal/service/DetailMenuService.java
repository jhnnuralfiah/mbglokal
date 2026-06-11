package com.kelompok7.mbglokal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kelompok7.mbglokal.entity.DetailMenu;
import com.kelompok7.mbglokal.repository.DetailMenuRepository;

@Service
public class DetailMenuService {

    private final DetailMenuRepository repository;

    public DetailMenuService(
            DetailMenuRepository repository
    ) {
        this.repository = repository;
    }

    public List<DetailMenu> getAll() {
        return repository.findAll();
    }

    public DetailMenu create(
            DetailMenu detailMenu
    ) {
        return repository.save(detailMenu);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<DetailMenu> getByPaketMenu(
            Long idMenu
    ) {
        return repository
                .findByPaketMenu_IdMenu(
                        idMenu
                );
    }
}