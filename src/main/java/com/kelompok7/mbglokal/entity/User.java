package com.kelompok7.mbglokal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Data 
@Entity
@Table(name = "users") 
@Inheritance(strategy = InheritanceType.JOINED) 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    // --- Atribut untuk Login ---
    @Column(unique = true)
    private String username;
    
    private String password;
    
    private String role;

    private String kontak;
    
    private String alamat;

    private String namaInstansi;
    
    private String namaPetani;
}