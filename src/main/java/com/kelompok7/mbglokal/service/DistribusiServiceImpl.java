package com.kelompok7.mbglokal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelompok7.mbglokal.entity.DetailMenu;
import com.kelompok7.mbglokal.entity.Distribusi;
import com.kelompok7.mbglokal.entity.Komoditas;
import com.kelompok7.mbglokal.repository.DetailMenuRepository;
import com.kelompok7.mbglokal.repository.DistribusiRepository;
import com.kelompok7.mbglokal.repository.KomoditasRepository;

@Service
public class DistribusiServiceImpl implements DistribusiService {

    @Autowired
    private DistribusiRepository repo;

    @Autowired
    private DetailMenuRepository detailMenuRepository;

    @Autowired
    private KomoditasRepository komoditasRepository;

    @Override
    public List<Distribusi> getAll() {
        return repo.findAll();
    }

    @Override
    public Distribusi getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Distribusi create(Distribusi distribusi) {

        // VALIDASI JUMLAH PORSI
        if (distribusi.getJumlahPorsiDikirim() <= 0) {
            throw new RuntimeException("Jumlah porsi harus lebih dari 0");
        }

        // AMBIL DETAIL MENU
        List<DetailMenu> detailMenus = detailMenuRepository.findByPaketMenu_IdMenu(
                distribusi.getPaketMenu().getIdMenu());

        // VALIDASI MENU
        if (detailMenus.isEmpty()) {
            throw new RuntimeException("Menu tidak memiliki detail bahan");
        }

        // CEK STOK (COLLECT ALL ERROR)
        List<String> errorList = new java.util.ArrayList<>();

        for (DetailMenu detail : detailMenus) {

            Komoditas komoditas = detail.getKomoditas();

            double kebutuhanTotal = detail.getJumlahKebutuhanPerPorsi()
                    * distribusi.getJumlahPorsiDikirim();

            if (komoditas.getStokSaatIni() == null) {

                errorList.add(
                        "Stok bahan belum diisi: " +
                                komoditas.getNamaBahan());

                continue;
            }

            if (komoditas.getStokSaatIni() < kebutuhanTotal) {

                errorList.add(
                        komoditas.getNamaBahan());
            }
        }

        // kalau ada error, stop semua proses
        if (!errorList.isEmpty()) {

            throw new RuntimeException(
                    "Stok tidak cukup untuk: " +
                            String.join(", ", errorList));
        }

        // KURANGI STOK
        for (DetailMenu detail : detailMenus) {

            Komoditas komoditas = detail.getKomoditas();

            double kebutuhanTotal = detail.getJumlahKebutuhanPerPorsi()
                    * distribusi.getJumlahPorsiDikirim();

            double stokBaru = komoditas.getStokSaatIni() - kebutuhanTotal;

            if (stokBaru < 0) {
                throw new RuntimeException(
                        "Stok menjadi negatif untuk: "
                                + komoditas.getNamaBahan());
            }

            komoditas.setStokSaatIni(stokBaru);

            komoditasRepository.save(komoditas);
        }

        // STATUS DEFAULT
        if (distribusi.getStatus() == null ||
                distribusi.getStatus().isEmpty()) {

            distribusi.setStatus("Proses");
        }

        // SIMPAN DISTRIBUSI
        return repo.save(distribusi);
    }

    @Override
    public Distribusi update(Long id, Distribusi distribusi) {

        Distribusi existing = repo.findById(id).orElseThrow();

        // Update field yang boleh diubah
        if (distribusi.getPaketMenu() != null) {
            existing.setPaketMenu(distribusi.getPaketMenu());
        }

        if (distribusi.getPenerimaManfaat() != null) {
            existing.setPenerimaManfaat(distribusi.getPenerimaManfaat());
        }

        if (distribusi.getTanggalKirim() != null) {
            existing.setTanggalKirim(distribusi.getTanggalKirim());
        }

        if (distribusi.getJumlahPorsiDikirim() > 0) {
            existing.setJumlahPorsiDikirim(distribusi.getJumlahPorsiDikirim());
        }

        if (distribusi.getStatus() != null) {
            existing.setStatus(distribusi.getStatus());
        }

        return repo.save(existing);
    }

    @Override
    public Distribusi updateStatus(Long id, String status) {

        Distribusi d = repo.findById(id).orElseThrow();

        d.setStatus(status);

        return repo.save(d);
    }

    @Override
    public void delete(Long id) {
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