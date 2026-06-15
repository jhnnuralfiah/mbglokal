const BASE_URL = "http://localhost:8080/api";

// ==========================
// PROTEKSI HALAMAN
// ==========================

function cekLogin() {
  const role = localStorage.getItem("role");
  const path = window.location.pathname;

  if (!role) {
    if (!path.includes("login.html") && !path.includes("register.html")) {
      window.location.href = "login.html";
    }
    return;
  }

  if (path.includes("admin-dashboard.html") && role !== "ADMIN") {
    window.location.href = "login.html";
  }

  if (path.includes("petani-dashboard.html") && role !== "PETANI") {
    window.location.href = "login.html";
  }

  if (path.includes("sekolah-dashboard.html") && role !== "SEKOLAH") {
    window.location.href = "login.html";
  }
}

// ==========================
// TOAST NOTIFICATION
// ==========================

function showToast(message, type) {
  const toastContainer = document.getElementById("toast");
  if (!toastContainer) return;

  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerText = message;
  toastContainer.appendChild(toast);

  setTimeout(() => {
    toast.remove();
  }, 3000);
}

// ==========================
// MODAL
// ==========================

function openModal(id) {
  document.getElementById(id).classList.add("active");
}

function closeModal(id) {
  document.getElementById(id).classList.remove("active");
}

// ==========================
// LOGIN
// ==========================

async function login() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  if (!username || !password) {
    showToast("Username dan password wajib diisi!", "error");
    return;
  }

  try {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    const data = await response.json();

    if (data.success) {
      showToast("Login berhasil!", "success");
      localStorage.setItem("role", data.role);
      localStorage.setItem("username", data.username);
      localStorage.setItem("idUser", data.idUser);

      setTimeout(() => {
        if (data.role === "ADMIN")
          window.location.href = "admin-dashboard.html";
        else if (data.role === "PETANI")
          window.location.href = "petani-dashboard.html";
        else if (data.role === "SEKOLAH")
          window.location.href = "sekolah-dashboard.html";
      }, 1200);
    } else {
      showToast(data.message, "error");
    }
  } catch (error) {
    console.log(error);
    showToast("Backend tidak terhubung!", "error");
  }
}

// ==========================
// LOGOUT
// ==========================

function logout() {
  showToast("Logout berhasil!", "success");
  localStorage.removeItem("role");
  localStorage.removeItem("username");
  localStorage.removeItem("idUser");
  localStorage.removeItem("lastPage");
  setTimeout(() => {
    window.location.href = "login.html";
  }, 1000);
}

// ==========================
// SHOW PAGE
// ==========================

function showPage(page, el) {
  document.querySelectorAll(".page").forEach((p) => p.classList.add("hidden"));

  const pageEl = document.getElementById(page);
  if (!pageEl) return;
  pageEl.classList.remove("hidden");

  document
    .querySelectorAll(".sidebar ul li")
    .forEach((li) => li.classList.remove("active"));
  if (el) el.classList.add("active");

  localStorage.setItem("lastPage", page);
}

// ==========================
// REGISTER
// ==========================

async function register() {
  const role = document.getElementById("role").value;
  const username = document.getElementById("username")?.value;
  const password = document.getElementById("password")?.value;

  if (!role || !username || !password) {
    showToast("Data belum lengkap", "error");
    return;
  }

  let data = { username, password, role };

  if (role === "PETANI") {
    data.namaPetani = document.getElementById("namaPetani")?.value;
  }
  if (role === "SEKOLAH") {
    data.namaInstansi = document.getElementById("namaInstansi")?.value;
  }

  try {
    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    const result = await res.json();

    if (!res.ok) {
      showToast(result.message, "error");
      return;
    }

    showToast("Register berhasil!", "success");
    setTimeout(() => {
      window.location.href = "login.html";
    }, 1200);
  } catch (err) {
    console.log(err);
    showToast("Server error", "error");
  }
}

function renderRegisterForm() {
  const role = document.getElementById("role").value;
  const form = document.getElementById("dynamicForm");
  form.innerHTML = "";

  if (role === "ADMIN") {
    form.innerHTML = `
            <input type="text" id="username" placeholder="Username">
            <input type="password" id="password" placeholder="Password">
        `;
  } else if (role === "PETANI") {
    form.innerHTML = `
            <input type="text" id="username" placeholder="Username">
            <input type="password" id="password" placeholder="Password">
            <input type="text" id="namaPetani" placeholder="Nama Petani">
        `;
  } else if (role === "SEKOLAH") {
    form.innerHTML = `
            <input type="text" id="username" placeholder="Username">
            <input type="password" id="password" placeholder="Password">
            <input type="text" id="namaInstansi" placeholder="Nama Sekolah">
        `;
  }
}

// ==========================
// KOMODITAS
// ==========================

async function loadKomoditas() {
  const body = document.getElementById("komoditasBody");
  if (!body) return;

  try {
    const response = await fetch(`${BASE_URL}/komoditas`);
    const data = await response.json();

    body.innerHTML = "";
    data.forEach((item, i) => {
      body.innerHTML += `
                <tr>
                    <td>${i + 1}</td>
                    <td>${item.namaBahan}</td>
                    <td>${item.stokSaatIni}</td>
                    <td>${item.satuan || "kg"}</td>
                    <td>
                        <div class="action-btn">
                            <button class="edit-btn" onclick="openEditKomoditas(${item.idKomoditas}, '${item.namaBahan}', ${item.stokSaatIni}, '${item.satuan || "kg"}')">✏️ Edit</button>
                            <button class="delete-btn" onclick="hapusKomoditas(${item.idKomoditas})">🗑️ Hapus</button>
                        </div>
                    </td>
                </tr>
            `;
    });

    const total = document.getElementById("totalKomoditas");
    if (total) total.innerText = data.length;
  } catch (error) {
    console.log(error);
    showToast("Gagal mengambil data komoditas", "error");
  }
}

async function tambahKomoditas() {
  const nama = document.getElementById("namaKomoditas").value;
  const stok = document.getElementById("stokKomoditas").value;
  const satuan = document.getElementById("satuanKomoditas").value || "kg";

  if (!nama || !stok) {
    showToast("Data komoditas belum lengkap", "error");
    return;
  }

  try {
    const res = await fetch(`${BASE_URL}/komoditas`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        namaBahan: nama,
        stokSaatIni: parseFloat(stok),
        satuan,
      }),
    });

    if (!res.ok) throw new Error();

    showToast("Komoditas berhasil ditambahkan", "success");
    loadKomoditas();
    document.getElementById("namaKomoditas").value = "";
    document.getElementById("stokKomoditas").value = "";
    document.getElementById("satuanKomoditas").value = "";
  } catch {
    showToast("Gagal tambah komoditas", "error");
  }
}

function openEditKomoditas(id, nama, stok, satuan) {
  document.getElementById("editKomoditasId").value = id;
  document.getElementById("editNamaKomoditas").value = nama;
  document.getElementById("editStokKomoditas").value = stok;
  document.getElementById("editSatuanKomoditas").value = satuan;
  openModal("modalEditKomoditas");
}

async function simpanEditKomoditas() {
  const id = document.getElementById("editKomoditasId").value;
  const nama = document.getElementById("editNamaKomoditas").value;
  const stok = document.getElementById("editStokKomoditas").value;
  const satuan = document.getElementById("editSatuanKomoditas").value;

  try {
    const res = await fetch(`${BASE_URL}/komoditas/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        namaBahan: nama,
        stokSaatIni: parseFloat(stok),
        satuan,
      }),
    });

    if (!res.ok) throw new Error();

    showToast("Komoditas berhasil diupdate", "success");
    closeModal("modalEditKomoditas");
    loadKomoditas();
  } catch {
    showToast("Gagal update komoditas", "error");
  }
}

async function hapusKomoditas(id) {
  if (!confirm("Yakin ingin menghapus komoditas ini?")) return;

  try {
    await fetch(`${BASE_URL}/komoditas/${id}`, { method: "DELETE" });
    showToast("Komoditas berhasil dihapus", "success");
    loadKomoditas();
  } catch {
    showToast("Gagal menghapus komoditas", "error");
  }
}

// ==========================
// PAKET MENU
// ==========================

async function loadPaket() {
  const body = document.getElementById("paketBody");
  if (!body) return;

  try {
    const response = await fetch(`${BASE_URL}/paket-menu`);
    const data = await response.json();

    body.innerHTML = "";
    data.forEach((item, i) => {
      body.innerHTML += `
                <tr>
                    <td>${i + 1}</td>
                    <td>${item.namaMenu}</td>
                    <td>${item.deskripsiGizi}</td>
                    <td>
                        <div class="action-btn">
                            <button class="edit-btn" onclick="openEditPaket(${item.idMenu}, '${item.namaMenu}', '${item.deskripsiGizi}')">✏️ Edit</button>
                            <button class="delete-btn" onclick="hapusPaket(${item.idMenu})">🗑️ Hapus</button>
                        </div>
                    </td>
                </tr>
            `;
    });

    const total = document.getElementById("totalPaket");
    if (total) total.innerText = data.length;
  } catch (error) {
    console.log(error);
    showToast("Gagal memuat data paket menu", "error");
  }
}

async function tambahPaket() {
  const nama = document.getElementById("namaPaket").value;
  const deskripsi = document.getElementById("isiPaket").value;

  if (!nama || !deskripsi) {
    showToast("Data paket menu belum lengkap", "error");
    return;
  }

  try {
    await fetch(`${BASE_URL}/paket-menu`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ namaMenu: nama, deskripsiGizi: deskripsi }),
    });

    showToast("Paket menu berhasil ditambahkan", "success");
    loadPaket();
    document.getElementById("namaPaket").value = "";
    document.getElementById("isiPaket").value = "";
  } catch {
    showToast("Gagal menambah paket", "error");
  }
}

function openEditPaket(id, nama, deskripsi) {
  document.getElementById("editPaketId").value = id;
  document.getElementById("editNamaPaket").value = nama;
  document.getElementById("editDeskripsiPaket").value = deskripsi;
  openModal("modalEditPaket");
}

async function simpanEditPaket() {
  const id = document.getElementById("editPaketId").value;
  const nama = document.getElementById("editNamaPaket").value;
  const deskripsi = document.getElementById("editDeskripsiPaket").value;

  try {
    const res = await fetch(`${BASE_URL}/paket-menu/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ namaMenu: nama, deskripsiGizi: deskripsi }),
    });

    if (!res.ok) throw new Error();

    showToast("Paket menu berhasil diupdate", "success");
    closeModal("modalEditPaket");
    loadPaket();
  } catch {
    showToast("Gagal update paket menu", "error");
  }
}

async function hapusPaket(id) {
  if (!confirm("Yakin ingin menghapus paket menu ini?")) return;

  try {
    await fetch(`${BASE_URL}/paket-menu/${id}`, { method: "DELETE" });
    showToast("Paket menu berhasil dihapus", "success");
    loadPaket();
  } catch {
    showToast("Gagal menghapus paket", "error");
  }
}

// ==========================
// DETAIL MENU
// ==========================

async function loadDetailMenu() {
  const body = document.getElementById("detailMenuBody");
  if (!body) return;

  try {
    const response = await fetch(`${BASE_URL}/detail-menu`);
    const data = await response.json();

    body.innerHTML = "";
    data.forEach((item, i) => {
      body.innerHTML += `
                <tr>
                    <td>${i + 1}</td>
                    <td>${item.paketMenu?.namaMenu || "-"}</td>
                    <td>${item.komoditas?.namaBahan || "-"}</td>
                    <td>${item.jumlahKebutuhanPerPorsi}</td>
                    <td>
                        <div class="action-btn">

    <button class="edit-btn"
        onclick="openEditDetailMenu(
            ${item.idDetail},
            ${item.paketMenu?.idMenu},
            ${item.komoditas?.idKomoditas},
            ${item.jumlahKebutuhanPerPorsi}
        )">
        ✏️ Edit
    </button>

    <button class="delete-btn"
        onclick="hapusDetailMenu(${item.idDetail})">
        🗑️ Hapus
    </button>

</div>
                    </td>
                </tr>
            `;
    });
  } catch {
    showToast("Gagal load detail menu", "error");
  }
}

async function tambahDetailMenu() {
  const idMenu = document.getElementById("idPaketDetail").value;
  const idKomoditas = document.getElementById("idKomoditasDetail").value;
  const jumlah = document.getElementById("jumlahKebutuhan").value;

  if (!idMenu || !idKomoditas || !jumlah) {
    showToast("Data belum lengkap", "error");
    return;
  }

  try {
    const res = await fetch(`${BASE_URL}/detail-menu`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        paketMenu: { idMenu: parseInt(idMenu) },
        komoditas: { idKomoditas: parseInt(idKomoditas) },
        jumlahKebutuhanPerPorsi: parseFloat(jumlah),
      }),
    });

    if (!res.ok) throw new Error();

    showToast("Detail menu berhasil ditambahkan", "success");
    loadDetailMenu();
    document.getElementById("idPaketDetail").value = "";
    document.getElementById("idKomoditasDetail").value = "";
    document.getElementById("jumlahKebutuhan").value = "";
  } catch {
    showToast("Gagal tambah detail menu", "error");
  }
}

async function openEditDetailMenu(id, idMenu, idKomoditas, jumlah) {
  document.getElementById("editDetailId").value = id;
  document.getElementById("editJumlahDetail").value = jumlah;

  const paketRes = await fetch(`${BASE_URL}/paket-menu`);
  const paketData = await paketRes.json();

  const paketSelect = document.getElementById("editPaketDetail");
  paketSelect.innerHTML = "";

  paketData.forEach((item) => {
    paketSelect.innerHTML += `
            <option value="${item.idMenu}">
                ${item.namaMenu}
            </option>
        `;
  });

  paketSelect.value = idMenu;

  const komoditasRes = await fetch(`${BASE_URL}/komoditas`);
  const komoditasData = await komoditasRes.json();

  const komoditasSelect = document.getElementById("editKomoditasDetail");
  komoditasSelect.innerHTML = "";

  komoditasData.forEach((item) => {
    komoditasSelect.innerHTML += `
            <option value="${item.idKomoditas}">
                ${item.namaBahan}
            </option>
        `;
  });

  komoditasSelect.value = idKomoditas;

  openModal("modalEditDetailMenu");
}

async function simpanEditDetailMenu() {
  const id = document.getElementById("editDetailId").value;

  const idMenu = document.getElementById("editPaketDetail").value;

  const idKomoditas = document.getElementById("editKomoditasDetail").value;

  const jumlah = document.getElementById("editJumlahDetail").value;

  try {
    const res = await fetch(`${BASE_URL}/detail-menu/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        paketMenu: {
          idMenu: parseInt(idMenu),
        },
        komoditas: {
          idKomoditas: parseInt(idKomoditas),
        },
        jumlahKebutuhanPerPorsi: parseFloat(jumlah),
      }),
    });

    if (!res.ok) throw new Error();

    showToast("Detail menu berhasil diupdate", "success");

    closeModal("modalEditDetailMenu");

    loadDetailMenu();
  } catch {
    showToast("Gagal update detail menu", "error");
  }
}

// ==========================
// DISTRIBUSI
// ==========================

async function loadDistribusi() {
  const body = document.getElementById("distribusiBody");

  if (!body) return;

  try {
    const response = await fetch(`${BASE_URL}/distribusi`);

    const data = await response.json();

    console.log(data);

    body.innerHTML = "";

    data.forEach((item, i) => {
      body.innerHTML += `
        <tr>
          <td>${i + 1}</td>
          <td>${item.paketMenu?.namaMenu || "-"}</td>
          <td>${item.penerimaManfaat?.namaInstansi || "-"}</td>
          <td>${item.tanggalKirim}</td>
          <td>${item.jumlahPorsiDikirim}</td>
          <td>${item.status}</td>

          <td>
            <button class="delete-btn"
              onclick="hapusDistribusi(${item.idDistribusi})">
              🗑️ Hapus
            </button>
          </td>

        </tr>
      `;
    });
  } catch (error) {
    console.log(error);

    showToast("Gagal load distribusi", "error");
  }

  async function tambahDistribusi() {

        const idMenu =
            document.getElementById("idMenuDistribusi").value;

        const idPenerima =
            document.getElementById("idPenerimaDistribusi").value;

        const tanggal =
            document.getElementById("tanggalDistribusi").value;

        const jumlah =
            document.getElementById("jumlahDistribusi").value;

        const status =
            document.getElementById("statusDistribusi").value;

        if (!idMenu || !idPenerima || !tanggal || !jumlah || !status) {

            showToast("Data distribusi belum lengkap", "error");

            return;
        }

        try {

            const res =
                await fetch(`${BASE_URL}/distribusi`, {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({

                        paketMenu: {
                            idMenu: parseInt(idMenu)
                        },

                        penerimaManfaat: {
                            idUser: parseInt(idPenerima)
                        },

                        tanggalKirim: tanggal,

                        jumlahPorsiDikirim: parseInt(jumlah),

                        status: status
                    })
                });

            if (!res.ok) {
                throw new Error();
            }

            showToast(
                "Distribusi berhasil ditambahkan",
                "success"
            );

            loadDistribusi();

        } catch(error) {

            console.log(error);

            showToast(
                "Gagal tambah distribusi",
                "error"
            );
        }
    }
}

// ==========================
// PERMINTAAN KE PETANI (ADMIN)
// ==========================

async function loadDropdownKomoditasPermintaan() {
  const select = document.getElementById("idKomoditasPermintaan");
  if (!select) return;

  try {
    const res = await fetch(`${BASE_URL}/komoditas`);
    const data = await res.json();

    select.innerHTML = `<option value="">Pilih Komoditas</option>`;
    data.forEach((item) => {
      select.innerHTML += `<option value="${item.idKomoditas}">${item.namaBahan}</option>`;
    });
  } catch (error) {
    console.log(error);

    console.log("Gagal load dropdown komoditas permintaan");
  }
}

let permintaanList = JSON.parse(localStorage.getItem("permintaan")) || [];

function tambahPermintaan() {
  const select = document.getElementById("idKomoditasPermintaan");
  const jumlah = document.getElementById("jumlahPermintaan").value;

  if (!select.value || !jumlah) {
    showToast("Data permintaan belum lengkap", "error");
    return;
  }

  const namaKomoditas = select.options[select.selectedIndex].text;

  const tanggal = new Date().toLocaleDateString("id-ID", {
    day: "2-digit",
    month: "long",
    year: "numeric",
  });

  permintaanList.push({
    id: Date.now(),
    komoditas: namaKomoditas,
    jumlah,
    tanggal,
    status: "Menunggu",
  });

  localStorage.setItem("permintaan", JSON.stringify(permintaanList));
  showToast("Permintaan berhasil dikirim", "success");
  loadPermintaanAdmin();

  select.value = "";
  document.getElementById("jumlahPermintaan").value = "";
}

function loadPermintaanAdmin() {
  const body = document.getElementById("permintaanBody");
  if (!body) return;

  permintaanList = JSON.parse(localStorage.getItem("permintaan")) || [];

  body.innerHTML = "";
  permintaanList.forEach((item, i) => {
    body.innerHTML += `
            <tr>
                <td>${i + 1}</td>
                <td>${item.komoditas}</td>
                <td>${item.jumlah}</td>
                <td>${item.tanggal || "-"}</td>
                <td>${item.status}</td>
                <td>
                    <div class="action-btn">
                        <button class="delete-btn" onclick="hapusPermintaan(${item.id})">🗑️ Hapus</button>
                    </div>
                </td>
            </tr>
        `;
  });
}

function hapusPermintaan(id) {
  if (!confirm("Yakin ingin menghapus permintaan ini?")) return;
  permintaanList = permintaanList.filter((p) => p.id !== id);
  localStorage.setItem("permintaan", JSON.stringify(permintaanList));
  showToast("Permintaan dihapus", "success");
  loadPermintaanAdmin();
}

// ==========================
// PERMINTAAN KE PETANI (PETANI)
// ==========================

function loadPermintaanPetani() {
  const body = document.getElementById("permintaanBody");
  if (!body) return;

  const permintaan = JSON.parse(localStorage.getItem("permintaan")) || [];

  body.innerHTML = "";

  if (permintaan.length === 0) {
    body.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center; color:#999; padding:20px;">
                    Belum ada permintaan dari admin
                </td>
            </tr>
        `;
    return;
  }

  permintaan.forEach((item, i) => {
    let statusClass = "status-menunggu";
    if (item.status === "Disetujui") statusClass = "status-disetujui";
    if (item.status === "Ditolak") statusClass = "status-ditolak";

    body.innerHTML += `
            <tr>
                <td>${i + 1}</td>
                <td>${item.komoditas}</td>
                <td>${item.jumlah}</td>
                <td>${item.tanggal || "-"}</td>
                <td><span class="${statusClass}">${item.status}</span></td>
                <td>
                    <div class="action-btn">
                        ${
                          item.status === "Menunggu"
                            ? `
                            <button class="edit-btn" style="background:green; width:auto; padding:6px 12px;" onclick="konfirmasiPermintaan(${item.id}, 'Disetujui')">✅ Setuju</button>
                            <button class="delete-btn" style="width:auto; padding:6px 12px;" onclick="konfirmasiPermintaan(${item.id}, 'Ditolak')">❌ Tolak</button>
                        `
                            : "-"
                        }
                    </div>
                </td>
            </tr>
        `;
  });
}

function konfirmasiPermintaan(id, status) {
  const permintaan = JSON.parse(localStorage.getItem("permintaan")) || [];
  const index = permintaan.findIndex((p) => p.id === id);

  if (index !== -1) {
    permintaan[index].status = status;
    localStorage.setItem("permintaan", JSON.stringify(permintaan));
    showToast(`Permintaan ${status}!`, "success");
    loadPermintaanPetani();
    loadDashboardPetani();
  }
}

// ==========================
// KOMENTAR
// ==========================

let komentarList = JSON.parse(localStorage.getItem("komentar")) || [];

function loadKomentar() {
  const body = document.getElementById("komentarBody");
  if (!body) return;

  body.innerHTML = "";
  komentarList.forEach((item, i) => {
    body.innerHTML += `
            <tr>
                <td>${i + 1}</td>
                <td>${item.sekolah}</td>
                <td>${item.isi}</td>
                <td>${item.tanggal || "-"}</td>
                <td>
                    <div class="action-btn">
                        <button class="delete-btn" onclick="hapusKomentar(${item.id})">🗑️ Hapus</button>
                    </div>
                </td>
            </tr>
        `;
  });
}

async function tambahKomentar() {
  const sekolah = document.getElementById("namaSekolah")?.value;
  const isi = document.getElementById("isiKomentar")?.value;

  if (!sekolah || !isi) {
    showToast("Data komentar belum lengkap", "error");
    return;
  }

  const tanggal = new Date().toLocaleDateString("id-ID", {
    day: "2-digit",
    month: "long",
    year: "numeric",
  });

  komentarList.push({ id: Date.now(), sekolah, isi, tanggal });
  localStorage.setItem("komentar", JSON.stringify(komentarList));
  showToast("Komentar berhasil dikirim", "success");
  loadKomentar();

  document.getElementById("namaSekolah").value = "";
  document.getElementById("isiKomentar").value = "";
}

function hapusKomentar(id) {
  if (!confirm("Yakin ingin menghapus komentar ini?")) return;
  komentarList = komentarList.filter((k) => k.id !== id);
  localStorage.setItem("komentar", JSON.stringify(komentarList));
  showToast("Komentar berhasil dihapus", "success");
  loadKomentar();
}

// ==========================
// TAMBAH USER
// ==========================

async function tambahUser() {
  const username = document.getElementById("newUsername").value;
  const password = document.getElementById("newPassword").value;
  const role = document.getElementById("newRole").value;
  const namaInstansi = document.getElementById("newNamaInstansi").value;
  const namaPetani = document.getElementById("newNamaPetani").value;

  if (!username || !password || !role) {
    showToast("Username, password, dan role wajib diisi!", "error");
    return;
  }

  if (role === "SEKOLAH" && !namaInstansi) {
    showToast("Nama sekolah wajib diisi!", "error");
    return;
  }

  if (role === "PETANI" && !namaPetani) {
    showToast("Nama petani wajib diisi!", "error");
    return;
  }
  console.log(namaInstansi);

  const payload = { username, password, role };
  if (namaInstansi) payload.namaInstansi = namaInstansi;
  if (namaPetani) payload.namaPetani = namaPetani;

  try {
    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    const result = await res.json();

    if (!res.ok) {
      showToast(result.message || "Gagal tambah user", "error");
      return;
    }

    showToast("User berhasil ditambahkan!", "success");
    loadUser();

    document.getElementById("newUsername").value = "";
    document.getElementById("newPassword").value = "";
    document.getElementById("newRole").value = "";
    document.getElementById("newNamaInstansi").value = "";
    document.getElementById("newNamaPetani").value = "";
  } catch {
    showToast("Gagal tambah user", "error");
  }
}

// ==========================
// USER MANAGEMENT
// ==========================

async function loadUser() {
  const body = document.getElementById("userBody");
  if (!body) return;

  const filter = document.getElementById("filterRole")?.value || "";

  try {
    const response = await fetch(`${BASE_URL}/users`);
    let data = await response.json();

    if (filter) {
      data = data.filter((u) => u.role === filter);
    }

    body.innerHTML = "";
    data.forEach((user, i) => {
      body.innerHTML += `
                <tr>
                    <td>${i + 1}</td>
                    <td>${user.username}</td>
                    <td>${user.role}</td>
                    <td>${user.namaInstansi || user.namaPetani || "-"}</td>
                    <td>
                        <div class="action-btn">
                            <button class="edit-btn" onclick="openEditUser(${user.idUser}, '${user.username}', '${user.role}', '${user.namaInstansi || ""}', '${user.namaPetani || ""}')">✏️ Edit</button>
                            <button class="delete-btn" onclick="hapusUser(${user.idUser})">🗑️ Hapus</button>
                        </div>
                    </td>
                </tr>
            `;
    });
  } catch {
    showToast("Gagal load user", "error");
  }
}

function openEditUser(id, username, role, namaInstansi, namaPetani) {
  document.getElementById("editUserId").value = id;
  document.getElementById("editUsername").value = username;
  document.getElementById("editRole").value = role;
  document.getElementById("editNamaInstansi").value = namaInstansi;
  document.getElementById("editNamaPetani").value = namaPetani;
  document.getElementById("editPassword").value = "";
  openModal("modalEditUser");
}

async function simpanEditUser() {
  const id = document.getElementById("editUserId").value;
  const username = document.getElementById("editUsername").value;
  const role = document.getElementById("editRole").value;
  const namaInstansi = document.getElementById("editNamaInstansi").value;
  const namaPetani = document.getElementById("editNamaPetani").value;
  const password = document.getElementById("editPassword").value;

  const payload = { username, role, namaInstansi, namaPetani };
  if (password) payload.password = password;

  try {
    const res = await fetch(`${BASE_URL}/users/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!res.ok) throw new Error();

    showToast("User berhasil diupdate", "success");
    closeModal("modalEditUser");
    loadUser();
  } catch {
    showToast("Gagal update user", "error");
  }
}

async function hapusUser(id) {
  if (!confirm("Yakin ingin menghapus user ini?")) return;

  try {
    await fetch(`${BASE_URL}/users/${id}`, { method: "DELETE" });
    showToast("User berhasil dihapus", "success");
    loadUser();
  } catch {
    showToast("Gagal hapus user", "error");
  }
}

// ==========================
// DROPDOWN
// ==========================

async function loadDropdownPaket() {
  const select = document.getElementById("idMenuDistribusi");

  if (!select) return;

  try {
    const res = await fetch(`${BASE_URL}/paket-menu`);

    const data = await res.json();

    console.log(data);

    select.innerHTML = `<option value="">Pilih Paket Menu</option>`;

    data.forEach((item) => {
      select.innerHTML += `
        <option value="${item.idMenu}">
          ${item.namaMenu}
        </option>
      `;
    });
  } catch (error) {
    console.log(error);
  }
}

async function loadDropdownPenerima() {
  const select = document.getElementById("idPenerimaDistribusi");

  if (!select) return;

  try {
    const res = await fetch(`${BASE_URL}/users`);

    const data = await res.json();

    console.log(data);

    select.innerHTML = `<option value="">Pilih Penerima</option>`;

    data.forEach((item) => {
      if (item.role === "SEKOLAH") {
        select.innerHTML += `
          <option value="${item.idUser}">
            ${item.namaInstansi || item.username}
          </option>
        `;
      }
    });
  } catch (error) {
    console.log(error);

    console.log("Gagal load dropdown penerima");
  }
}

async function loadDropdownDetailMenu() {
  const paketSelect = document.getElementById("idPaketDetail");
  const komoditasSelect = document.getElementById("idKomoditasDetail");
  if (!paketSelect || !komoditasSelect) return;

  try {
    const paketRes = await fetch(`${BASE_URL}/paket-menu`);
    const paketData = await paketRes.json();

    paketSelect.innerHTML = `<option value="">Pilih Paket Menu</option>`;
    paketData.forEach((item) => {
      paketSelect.innerHTML += `<option value="${item.idMenu}">${item.namaMenu}</option>`;
    });

    const komoditasRes = await fetch(`${BASE_URL}/komoditas`);
    const komoditasData = await komoditasRes.json();

    console.log(komoditasData);

    komoditasSelect.innerHTML = `<option value="">Pilih Komoditas</option>`;

    komoditasData.forEach((item) => {
      console.log(item);

      komoditasSelect.innerHTML += `
        <option value="${item.idKomoditas}">
            ${item.namaBahan}
        </option>
    `;
    });
  } catch {
    console.log("Gagal load dropdown detail menu");
  }
}

// ==========================
// DASHBOARD PREVIEW
// ==========================

async function loadDashboardPreview() {
  const body = document.getElementById("dashboardDistribusiPreview");
  if (!body) return;

  try {
    const res = await fetch(`${BASE_URL}/distribusi`);
    const data = await res.json();

    body.innerHTML = "";
    data.slice(0, 5).forEach((item) => {
      body.innerHTML += `
                <tr>
                    <td>${item.paketMenu?.namaMenu || "-"}</td>
                    <td>${item.penerimaManfaat?.namaInstansi || "-"}</td>
                    <td>${item.jumlahPorsiDikirim}</td>
                    <td>${item.status}</td>
                </tr>
            `;
    });
  } catch {
    console.log("Gagal load dashboard preview");
  }
}

// ==========================
// DASHBOARD PETANI
// ==========================

async function loadDashboardPetani() {
  const idUser = localStorage.getItem("idUser");

  try {
    const response = await fetch(`${BASE_URL}/komoditas`);
    const data = await response.json();

    const mine = data.filter(
      (item) => item.petani?.idUser == idUser || item.petani == null,
    );

    const totalEl = document.getElementById("totalKomoditas");
    const stokMenipisEl = document.getElementById("stokMenipis");
    const stokTerendahEl = document.getElementById("stokTerendah");
    const totalPermintaanEl = document.getElementById("totalPermintaan");

    if (totalEl) totalEl.innerText = mine.length;

    const lowStock = mine.filter((item) => item.stokSaatIni <= 10);
    if (stokMenipisEl) stokMenipisEl.innerText = lowStock.length;

    if (stokTerendahEl) {
      if (mine.length > 0) {
        const lowest = mine.reduce((a, b) =>
          a.stokSaatIni < b.stokSaatIni ? a : b,
        );
        stokTerendahEl.innerText = `${lowest.namaBahan} (${lowest.stokSaatIni} ${lowest.satuan || "kg"})`;
      } else {
        stokTerendahEl.innerText = "Belum ada stok";
      }
    }

    const permintaan = JSON.parse(localStorage.getItem("permintaan")) || [];
    const aktif = permintaan.filter((p) => p.status === "Menunggu");
    if (totalPermintaanEl) totalPermintaanEl.innerText = aktif.length;
  } catch {
    showToast("Gagal load dashboard petani", "error");
  }
}

async function loadKomoditasPetani() {
  const body = document.getElementById("komoditasBody");
  if (!body) return;

  const idUser = localStorage.getItem("idUser");

  try {
    const response = await fetch(`${BASE_URL}/komoditas`);
    const data = await response.json();

    const filtered = data.filter(
      (item) => item.petani?.idUser == idUser || item.petani == null,
    );

    body.innerHTML = "";
    filtered.forEach((item, i) => {
      body.innerHTML += `
                <tr>
                    <td>${i + 1}</td>
                    <td>${item.namaBahan}</td>
                    <td>${item.stokSaatIni}</td>
                    <td>${item.satuan || "kg"}</td>
                    <td>
                        <div class="action-btn">
                            <button class="edit-btn" onclick="openEditStok(${item.idKomoditas}, '${item.namaBahan}', ${item.stokSaatIni}, '${item.satuan || "kg"}')">✏️ Edit</button>
                            <button class="delete-btn" onclick="hapusKomoditas(${item.idKomoditas})">🗑️ Hapus</button>
                        </div>
                    </td>
                </tr>
            `;
    });

    const total = document.getElementById("totalKomoditas");
    if (total) total.innerText = filtered.length;
  } catch {
    showToast("Gagal load stok petani", "error");
  }
}

function openEditStok(id, nama, stok, satuan) {
  document.getElementById("editStokId").value = id;
  document.getElementById("editNamaStok").value = nama;
  document.getElementById("editJumlahStok").value = stok;
  document.getElementById("editSatuanStok").value = satuan;
  openModal("modalEditStok");
}

async function simpanEditStok() {
  const id = document.getElementById("editStokId").value;
  const nama = document.getElementById("editNamaStok").value;
  const stok = document.getElementById("editJumlahStok").value;
  const satuan = document.getElementById("editSatuanStok").value;

  try {
    const res = await fetch(`${BASE_URL}/komoditas/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        namaBahan: nama,
        stokSaatIni: parseFloat(stok),
        satuan,
      }),
    });

    if (!res.ok) throw new Error();

    showToast("Stok berhasil diupdate", "success");
    closeModal("modalEditStok");
    loadKomoditasPetani();
    loadDashboardPetani();
  } catch {
    showToast("Gagal update stok", "error");
  }
}

// ==========================
// DASHBOARD SEKOLAH
// ==========================

async function loadDashboardSekolah() {
  const idUser = localStorage.getItem("idUser");

  try {
    const response = await fetch(`${BASE_URL}/distribusi`);
    const data = await response.json();

    const mine = data.filter((item) => item.penerimaManfaat?.idUser == idUser);

    const totalEl = document.getElementById("totalDistribusi");
    const prosesEl = document.getElementById("prosesDistribusi");
    const selesaiEl = document.getElementById("selesaiDistribusi");
    const statusEl = document.getElementById("statusTerbaru");

    if (totalEl) totalEl.innerText = mine.length;
    if (prosesEl)
      prosesEl.innerText = mine.filter((d) => d.status === "Proses").length;
    if (selesaiEl)
      selesaiEl.innerText = mine.filter((d) => d.status === "Selesai").length;

    if (statusEl && mine.length > 0) {
      const latest = mine[mine.length - 1];
      statusEl.innerText = `${latest.paketMenu?.namaMenu || "-"} - ${latest.status}`;
    }
  } catch {
    showToast("Gagal load dashboard sekolah", "error");
  }
}

// ==========================
// AUTO LOAD
// ==========================

window.onload = function () {
  cekLogin();

  const role = localStorage.getItem("role");
  const username = localStorage.getItem("username");

  const welcomeEl = document.getElementById("welcomeAdmin");
  if (welcomeEl) welcomeEl.innerText = `👋 Halo, ${username}`;

  const lastPage = localStorage.getItem("lastPage") || "dashboard";
  showPage(lastPage);

  if (role === "ADMIN") {
    loadDashboardPreview();
    loadDetailMenu();
    loadDropdownDetailMenu();
    loadKomoditas();
    loadPaket();
    loadDistribusi();

    loadKomentar();
    loadDropdownPaket();
    loadDropdownPenerima();
    loadDropdownKomoditasPermintaan();
    loadPermintaanAdmin(); // ✅ versi admin
    loadUser();
  }

  if (role === "PETANI") {
    const welcomePetani = document.getElementById("welcomePetani");
    if (welcomePetani) welcomePetani.innerText = `👋 Halo, ${username}`;

    loadDashboardPetani();
    loadKomoditasPetani();
    loadPermintaanPetani();
  }

  if (role === "SEKOLAH") {
    const welcomeSekolah = document.getElementById("welcomeSekolah");
    if (welcomeSekolah) welcomeSekolah.innerText = `👋 Halo, ${username}`;

    loadDashboardSekolah();
    loadDistribusi();
    loadKomentar();
  }
};
