package com.kelompok7.mbglokal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "detail_menu")
public class DetailMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetail;

    @ManyToOne
    @JoinColumn(name = "id_menu")
    private PaketMenu paketMenu;

    @ManyToOne
    @JoinColumn(name = "id_komoditas")
    @JsonIgnoreProperties("petani")
    private Komoditas komoditas;

    @Column(name = "jumlah_kebutuhan_per_porsi")
    private Double jumlahKebutuhanPerPorsi;
}