package com.kelompok7.mbglokal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "penerima_manfaat")
public class PenerimaManfaat extends User { // Menerapkan Inheritance dari User
    
    @Column(name = "nama_instansi")
    private String namaInstansi;

    private String kategori; // Sekolah, Panti Asuhan
    
    @Column(name = "jumlah_porsi_harian")
    private Integer jumlahPorsiHarian;
}