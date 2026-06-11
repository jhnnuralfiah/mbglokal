package com.kelompok7.mbglokal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelompok7.mbglokal.entity.DetailMenu;

public interface DetailMenuRepository
        extends JpaRepository<DetailMenu, Long> {

    List<DetailMenu> findByPaketMenu_IdMenu(Long idMenu);
}