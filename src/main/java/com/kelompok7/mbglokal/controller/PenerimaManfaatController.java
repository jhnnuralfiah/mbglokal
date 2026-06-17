package com.kelompok7.mbglokal.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.entity.PenerimaManfaat;
import com.kelompok7.mbglokal.repository.UserRepository;
import com.kelompok7.mbglokal.service.PenerimaManfaatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/penerima-manfaat")
@RequiredArgsConstructor
public class PenerimaManfaatController {

    private final PenerimaManfaatService penerimaManfaatService;
    private final UserRepository userRepository;

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

    @GetMapping("/dropdown")
    public List<Map<String, Object>> getDropdown() {
        return penerimaManfaatService.getAllPenerimaManfaat().stream()
            .map(p -> {
                String namaInstansi = userRepository.findNamaInstansiById(p.getIdUser());
                String nama = (namaInstansi != null && !namaInstansi.isBlank())
                    ? namaInstansi
                    : p.getUsername();

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("idUser", p.getIdUser());
                map.put("namaInstansi", nama);
                return map;
            })
            .collect(Collectors.toList());
    }
}