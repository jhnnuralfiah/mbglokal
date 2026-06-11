package com.kelompok7.mbglokal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelompok7.mbglokal.entity.Distribusi;

public interface DistribusiRepository extends JpaRepository<Distribusi, Long> {

    List<Distribusi> findByStatus(String status);
    List<Distribusi> findByPenerimaManfaat_IdUser(Long idUser);
}