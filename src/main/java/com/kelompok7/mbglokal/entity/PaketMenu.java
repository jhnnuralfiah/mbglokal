package com.kelompok7.mbglokal.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "paket_menu")
public class PaketMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMenu;

    @Column(name = "nama_menu")
    private String namaMenu;

    @Column(name = "deskripsi_gizi")
    private String deskripsiGizi;

    @OneToMany(mappedBy = "paketMenu")
    @JsonIgnore
    private List<DetailMenu> detailMenus;
}