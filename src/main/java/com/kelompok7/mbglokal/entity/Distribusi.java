package com.kelompok7.mbglokal.entity;

import java.time.LocalDate;

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
@Table(name = "distribusi")
public class Distribusi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDistribusi;

    @ManyToOne
    @JoinColumn(name = "id_penerima")
    private PenerimaManfaat penerimaManfaat;

    @ManyToOne
    @JoinColumn(name = "id_menu")
    private PaketMenu paketMenu;

    @Column(name = "tanggal_kirim")
    private LocalDate tanggalKirim;

    @Column(name = "jumlah_porsi_dikirim")
    private Integer jumlahPorsiDikirim;

    private String status; 
}