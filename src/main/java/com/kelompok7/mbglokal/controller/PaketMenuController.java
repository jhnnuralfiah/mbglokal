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

import com.kelompok7.mbglokal.entity.PaketMenu;
import com.kelompok7.mbglokal.service.PaketMenuService;

@RestController
@RequestMapping("/api/paket-menu")
public class PaketMenuController {

    private final PaketMenuService paketMenuService;

    public PaketMenuController(PaketMenuService paketMenuService) {
        this.paketMenuService = paketMenuService;
    }

    @GetMapping
    public List<PaketMenu> getAllPaketMenu() {
        return paketMenuService.getAllPaketMenu();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaketMenu> getPaketMenuById(@PathVariable Long id) {
        Optional<PaketMenu> menu = paketMenuService.getPaketMenuById(id);
        return menu.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PaketMenu createPaketMenu(@RequestBody PaketMenu paketMenu) {
        return paketMenuService.createPaketMenu(paketMenu);
    }

    // ✅ TAMBAH ENDPOINT PUT
    @PutMapping("/{id}")
    public ResponseEntity<PaketMenu> updatePaketMenu(
            @PathVariable Long id,
            @RequestBody PaketMenu paketMenu) {

        Optional<PaketMenu> existing = paketMenuService.getPaketMenuById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaketMenu data = existing.get();
        data.setNamaMenu(paketMenu.getNamaMenu());
        data.setDeskripsiGizi(paketMenu.getDeskripsiGizi());

        return ResponseEntity.ok(paketMenuService.createPaketMenu(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaketMenu(@PathVariable Long id) {
        paketMenuService.deletePaketMenu(id);
        return ResponseEntity.ok().build();
    }
}