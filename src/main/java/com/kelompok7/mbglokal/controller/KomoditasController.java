package com.kelompok7.mbglokal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.entity.Komoditas;
import com.kelompok7.mbglokal.service.KomoditasService;

@RestController
@RequestMapping("/api/komoditas")
public class KomoditasController {

    private final KomoditasService komoditasService;

    public KomoditasController(KomoditasService komoditasService) {
        this.komoditasService = komoditasService;
    }

    @GetMapping
    public List<Komoditas> getAllKomoditas() {
        return komoditasService.getAllKomoditas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Komoditas> getKomoditasById(@PathVariable Long id) {
        Optional<Komoditas> komoditas = komoditasService.getKomoditasById(id);
        return komoditas.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Komoditas createKomoditas(@RequestBody Komoditas komoditas) {
        return komoditasService.createKomoditas(komoditas);
    }

    // ✅ TAMBAH ENDPOINT PUT
    @PutMapping("/{id}")
    public ResponseEntity<Komoditas> updateKomoditas(
            @PathVariable Long id,
            @RequestBody Komoditas komoditas) {

        Optional<Komoditas> existing = komoditasService.getKomoditasById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Komoditas data = existing.get();
        data.setNamaBahan(komoditas.getNamaBahan());
        data.setStokSaatIni(komoditas.getStokSaatIni());
        data.setSatuan(komoditas.getSatuan());

        return ResponseEntity.ok(komoditasService.createKomoditas(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKomoditas(@PathVariable Long id) {
        komoditasService.deleteKomoditas(id);
        return ResponseEntity.ok().build();
    }
}