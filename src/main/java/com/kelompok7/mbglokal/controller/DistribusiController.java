package com.kelompok7.mbglokal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kelompok7.mbglokal.entity.Distribusi;
import com.kelompok7.mbglokal.service.DistribusiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/distribusi")
@RequiredArgsConstructor
public class DistribusiController {

    private final DistribusiService service;

    @GetMapping
    public List<Distribusi> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Distribusi getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Distribusi create(@RequestBody Distribusi distribusi) {
        return service.create(distribusi);
    }

    @PutMapping("/{id}")
    public Distribusi updateDistribusi(
            @PathVariable Long id,
            @RequestBody Distribusi distribusi) {
        return service.update(id, distribusi);
    }

    @PutMapping("/{id}/status")
    public Distribusi updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/status/{status}")
    public List<Distribusi> getByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }

    @GetMapping("/user/{idUser}")
    public List<Distribusi> getByUser(@PathVariable Long idUser) {
        return service.getByUser(idUser);
    }
}