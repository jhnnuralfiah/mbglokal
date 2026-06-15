package com.kelompok7.mbglokal.controller;

import com.kelompok7.mbglokal.entity.DetailMenu;
import com.kelompok7.mbglokal.service.DetailMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detail-menu")
public class DetailMenuController {

    private final DetailMenuService service;

    public DetailMenuController(
            DetailMenuService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<DetailMenu> getAll() {
        return service.getAll();
    }

    @GetMapping("/paket/{idMenu}")
    public List<DetailMenu> getByPaket(
            @PathVariable Long idMenu
    ) {
        return service.getByPaketMenu(
                idMenu
        );
    }

    @PostMapping
    public DetailMenu create(
            @RequestBody DetailMenu detailMenu
    ) {
        return service.create(detailMenu);
    }

    @PutMapping("/{id}")
    public DetailMenu update(
            @PathVariable Long id,
            @RequestBody DetailMenu detailMenu
    ) {
        return service.update(id, detailMenu);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        service.delete(id);
    }
}