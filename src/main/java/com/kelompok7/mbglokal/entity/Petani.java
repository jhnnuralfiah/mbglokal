package com.kelompok7.mbglokal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "petani")
public class Petani extends User { // Menerapkan Inheritance dari User
    
    @Column(name = "nama_kelompok")
    private String namaKelompok;
}