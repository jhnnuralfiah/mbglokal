package com.kelompok7.mbglokal.seeder;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kelompok7.mbglokal.entity.DetailMenu;
import com.kelompok7.mbglokal.entity.Distribusi;
import com.kelompok7.mbglokal.entity.Komoditas;
import com.kelompok7.mbglokal.entity.PaketMenu;
import com.kelompok7.mbglokal.entity.PenerimaManfaat;
import com.kelompok7.mbglokal.entity.Petani;
import com.kelompok7.mbglokal.entity.User;
import com.kelompok7.mbglokal.repository.DetailMenuRepository;
import com.kelompok7.mbglokal.repository.DistribusiRepository;
import com.kelompok7.mbglokal.repository.KomoditasRepository;
import com.kelompok7.mbglokal.repository.PaketMenuRepository;
import com.kelompok7.mbglokal.repository.PenerimaManfaatRepository;
import com.kelompok7.mbglokal.repository.PetaniRepository;
import com.kelompok7.mbglokal.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PetaniRepository petaniRepository;
    private final PenerimaManfaatRepository penerimaManfaatRepository;
    private final PaketMenuRepository paketMenuRepository;
    private final KomoditasRepository komoditasRepository;
    private final DistribusiRepository distribusiRepository; 
    private final DetailMenuRepository detailMenuRepository; 
    private final UserRepository userRepository;

    public DataSeeder(PetaniRepository petaniRepository,
                      PenerimaManfaatRepository penerimaManfaatRepository,
                      PaketMenuRepository paketMenuRepository,
                      KomoditasRepository komoditasRepository,
                      DistribusiRepository distribusiRepository,
                      DetailMenuRepository detailMenuRepository,
                      UserRepository userRepository) { 
        this.petaniRepository = petaniRepository;
        this.penerimaManfaatRepository = penerimaManfaatRepository;
        this.paketMenuRepository = paketMenuRepository;
        this.komoditasRepository = komoditasRepository;
        this.distribusiRepository = distribusiRepository;
        this.detailMenuRepository = detailMenuRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (petaniRepository.count() == 0) {
            System.out.println("Memulai proses Seeding Data Dummy...");

            // 1. Data Petani 
            Petani petani = new Petani();
            petani.setNamaKelompok("Tani Makmur Jaya");
            petani.setKontak("089876543210"); 
            petani.setAlamat("Desa Ciburuy, Padalarang");
            petani.setUsername("petani1");
            petani.setPassword("12345");
            petani.setRole("PETANI");
            petaniRepository.save(petani);

            // 2. Data Penerima Manfaat 
            PenerimaManfaat panti = new PenerimaManfaat();
            panti.setNamaInstansi("SDN 01 Sejahtera");
            panti.setKategori("Sekolah");
            panti.setKontak("081122334455");
            panti.setAlamat("Jl. Merdeka No. 10, Bandung");
            panti.setJumlahPorsiHarian(150);
            panti.setUsername("sekolah1");
            panti.setPassword("12345");
            panti.setRole("PENERIMA");
            penerimaManfaatRepository.save(panti);

            // 3. Data Paket Menu MBG
            PaketMenu menu1 = new PaketMenu();
            menu1.setNamaMenu("Paket 4 Sehat (Ayam + Sayur)");
            menu1.setDeskripsiGizi("Tinggi Protein 600 kkal");
            paketMenuRepository.save(menu1);

            // 4. Data Komoditas
            Komoditas beras = new Komoditas();
            beras.setNamaBahan("Beras Putih Premium");
            beras.setKategori("Karbohidrat");
            beras.setStokSaatIni(500.0);
            beras.setSatuan("kg");
            komoditasRepository.save(beras);

            // 5. Data Detail Menu (Resep)
            DetailMenu resepBeras = new DetailMenu();
            resepBeras.setPaketMenu(menu1); 
            resepBeras.setKomoditas(beras); 
            resepBeras.setJumlahKebutuhanPerPorsi(0.15); 
            detailMenuRepository.save(resepBeras);

            // 6. Data Distribusi
            Distribusi distribusi = new Distribusi();
            distribusi.setPenerimaManfaat(panti);
            distribusi.setPaketMenu(menu1);
            distribusi.setTanggalKirim(LocalDate.now());
            distribusi.setJumlahPorsiDikirim(150);
            distribusi.setStatus("Dikirim");
            distribusiRepository.save(distribusi);

            // 7. Data Admin
            User admin = new User();
            admin.setUsername("admin_mbg");
            admin.setPassword("rahasia123");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            System.out.println("✅ Data Seeder berhasil! Database sudah terisi data dummy lengkap dengan Akun Login.");
        } else {
            System.out.println("⏩ Data sudah ada di database, Seeder di-skip.");
        }
    }
}