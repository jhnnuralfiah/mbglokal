package com.kelompok7.mbglokal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.entity.PenerimaManfaat;
import com.kelompok7.mbglokal.service.PenerimaManfaatService;

@RestController
@RequestMapping("/api/penerima-manfaat")
public class PenerimaManfaatController {

    private final PenerimaManfaatService penerimaManfaatService;

    public PenerimaManfaatController(PenerimaManfaatService penerimaManfaatService) {
        this.penerimaManfaatService = penerimaManfaatService;
    }

    @GetMapping
    public List<PenerimaManfaat> getAllPenerimaManfaat() {
        return penerimaManfaatService.getAllPenerimaManfaat();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PenerimaManfaat> getPenerimaManfaatById(@PathVariable Long id) {
        Optional<PenerimaManfaat> penerima = penerimaManfaatService.getPenerimaManfaatById(id);
        return penerima.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PenerimaManfaat createPenerimaManfaat(@RequestBody PenerimaManfaat penerimaManfaat) {
        return penerimaManfaatService.createPenerimaManfaat(penerimaManfaat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePenerimaManfaat(@PathVariable Long id) {
        penerimaManfaatService.deletePenerimaManfaat(id);
        return ResponseEntity.ok().build();
    }
}