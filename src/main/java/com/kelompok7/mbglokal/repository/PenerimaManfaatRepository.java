package com.kelompok7.mbglokal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kelompok7.mbglokal.entity.PenerimaManfaat;

@Repository
public interface PenerimaManfaatRepository extends JpaRepository<PenerimaManfaat, Long> {
}