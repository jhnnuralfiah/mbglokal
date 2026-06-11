package com.kelompok7.mbglokal.service;

import java.util.List;

import com.kelompok7.mbglokal.entity.Distribusi;

public interface DistribusiService {

    List<Distribusi> getAll();

    Distribusi getById(Long id);

    Distribusi create(Distribusi distribusi);

    Distribusi update(Long id, Distribusi distribusi); 

    Distribusi updateStatus(Long id, String status);

    void delete(Long id);

    List<Distribusi> getByStatus(String status);

    List<Distribusi> getByUser(Long idUser);
}