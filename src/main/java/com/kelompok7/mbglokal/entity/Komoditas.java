package com.kelompok7.mbglokal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "komoditas")
public class Komoditas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKomoditas;

    @Column(name = "nama_bahan")
    private String namaBahan;

    private String kategori; // Karbohidrat, Protein, Sayur
    
    @Column(name = "stok_saat_ini")
    private Double stokSaatIni;

    private String satuan; // kg, liter, butir

    // Relasi Many-to-One: Banyak komoditas bisa disuplai oleh 1 petani
    @ManyToOne
    @JoinColumn(name = "id_petani")
    private Petani petani;
}