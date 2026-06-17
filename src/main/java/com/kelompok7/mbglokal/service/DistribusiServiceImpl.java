package com.kelompok7.mbglokal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelompok7.mbglokal.entity.DetailMenu;
import com.kelompok7.mbglokal.entity.Distribusi;
import com.kelompok7.mbglokal.entity.Komoditas;
import com.kelompok7.mbglokal.exception.BusinessException;
import com.kelompok7.mbglokal.exception.ResourceNotFoundException;
import com.kelompok7.mbglokal.repository.DetailMenuRepository;
import com.kelompok7.mbglokal.repository.DistribusiRepository;
import com.kelompok7.mbglokal.repository.KomoditasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistribusiServiceImpl implements DistribusiService {

    private final DistribusiRepository repo;
    private final DetailMenuRepository detailMenuRepository;
    private final KomoditasRepository komoditasRepository;

    @Override
    public List<Distribusi> getAll() {
        return repo.findAll();
    }

    @Override
    public Distribusi getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribusi tidak ditemukan dengan id: " + id));
    }

    @Override
    @Transactional
    public Distribusi create(Distribusi distribusi) {

        if (distribusi.getJumlahPorsiDikirim() == null || distribusi.getJumlahPorsiDikirim() <= 0) {
            throw new BusinessException("Jumlah porsi harus lebih dari 0");
        }

        if (distribusi.getPaketMenu() == null || distribusi.getPaketMenu().getIdMenu() == null) {
            throw new BusinessException("Paket menu wajib diisi");
        }

        List<DetailMenu> detailMenus = detailMenuRepository.findByPaketMenu_IdMenu(
                distribusi.getPaketMenu().getIdMenu());

        if (detailMenus.isEmpty()) {
            throw new BusinessException("Menu tidak memiliki detail bahan");
        }

        List<String> errorList = new ArrayList<>();

        for (DetailMenu detail : detailMenus) {
            Komoditas komoditas = detail.getKomoditas();
            double kebutuhanTotal = detail.getJumlahKebutuhanPerPorsi() * distribusi.getJumlahPorsiDikirim();

            if (komoditas.getStokSaatIni() == null) {
                errorList.add("Stok bahan belum diisi: " + komoditas.getNamaBahan());
                continue;
            }

            if (komoditas.getStokSaatIni() < kebutuhanTotal) {
                errorList.add(komoditas.getNamaBahan());
            }
        }

        if (!errorList.isEmpty()) {
            throw new BusinessException("Stok tidak cukup untuk: " + String.join(", ", errorList));
        }

        for (DetailMenu detail : detailMenus) {
            Komoditas komoditas = detail.getKomoditas();
            double kebutuhanTotal = detail.getJumlahKebutuhanPerPorsi() * distribusi.getJumlahPorsiDikirim();
            double stokBaru = komoditas.getStokSaatIni() - kebutuhanTotal;

            if (stokBaru < 0) {
                throw new BusinessException("Stok menjadi negatif untuk: " + komoditas.getNamaBahan());
            }

            komoditas.setStokSaatIni(stokBaru);
            komoditasRepository.save(komoditas);
        }

        if (distribusi.getStatus() == null || distribusi.getStatus().isEmpty()) {
            distribusi.setStatus("Proses");
        }

        return repo.save(distribusi);
    }

    @Override
    public Distribusi update(Long id, Distribusi distribusi) {

        Distribusi existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribusi tidak ditemukan dengan id: " + id));

        if (distribusi.getPenerimaManfaat() != null) {
            existing.setPenerimaManfaat(distribusi.getPenerimaManfaat());
        }

        if (distribusi.getTanggalKirim() != null) {
            existing.setTanggalKirim(distribusi.getTanggalKirim());
        }

        if (distribusi.getStatus() != null) {
            existing.setStatus(distribusi.getStatus());
        }

        return repo.save(existing);
    }

    @Override
    public Distribusi updateStatus(Long id, String status) {

        Distribusi d = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribusi tidak ditemukan dengan id: " + id));

        d.setStatus(status);

        return repo.save(d);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Distribusi tidak ditemukan dengan id: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public List<Distribusi> getByStatus(String status) {
        return repo.findByStatus(status);
    }

    @Override
    public List<Distribusi> getByUser(Long idUser) {
        return repo.findByPenerimaManfaat_IdUser(idUser);
    }
}