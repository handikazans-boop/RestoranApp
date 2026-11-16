import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Program utama: manajemen restoran berbasis objek (menu, pesanan, struk, file). */
public class Main {

    static final String FILE_MENU = "menu.txt";

    static Scanner scanner = new Scanner(System.in);
    static Menu menu = new Menu();
    static Pesanan pesananAktif = new Pesanan();

    public static void main(String[] args) {
        siapkanMenu();

        boolean jalan = true;
        while (jalan) {
            tampilMenuUtama();
            int pilih = bacaAngka("Pilih menu (0-8): ", 0, 8);

            switch (pilih) {
                case 1:
                    tambahItemBaru();
                    break;
                case 2:
                    menu.tampilkanMenu();
                    break;
                case 3:
                    terimaPesanan();
                    break;
                case 4:
                    hitungTotal();
                    break;
                case 5:
                    tampilkanStruk();
                    break;
                case 6:
                    simpanMenu();
                    break;
                case 7:
                    muatMenu();
                    break;
                case 8:
                    muatStruk();
                    break;
                case 0:
                    System.out.println("Terima kasih, sampai jumpa!");
                    jalan = false;
                    break;
            }
        }
        scanner.close();
    }

    static void tampilMenuUtama() {
        System.out.println("\n========= MANAJEMEN RESTORAN =========");
        System.out.println("1. Tambah item baru ke menu");
        System.out.println("2. Tampilkan menu restoran");
        System.out.println("3. Terima pesanan pelanggan");
        System.out.println("4. Hitung total biaya pesanan");
        System.out.println("5. Tampilkan struk pesanan");
        System.out.println("6. Simpan menu ke file");
        System.out.println("7. Muat menu dari file");
        System.out.println("8. Muat struk dari file");
        System.out.println("0. Keluar");
    }

    // ================== MENU ==================

    /** Saat program mulai: muat menu.txt kalau ada, kalau tidak pakai menu awal. */
    static void siapkanMenu() {
        try {
            int dilewati = menu.muatDariFile(FILE_MENU);
            System.out.println("Menu dimuat dari " + FILE_MENU + " (" + menu.getJumlahItem() + " item).");
            if (dilewati > 0) {
                System.out.println(dilewati + " baris dilewati karena formatnya salah.");
            }
        } catch (FileNotFoundException e) {
            menu.isiDataAwal();
            System.out.println("File " + FILE_MENU + " belum ada, memakai menu awal.");
        } catch (IOException e) {
            menu.isiDataAwal();
            System.out.println("Gagal memuat menu (" + e.getMessage() + "), memakai menu awal.");
        }
    }

    static void tambahItemBaru() {
        System.out.println("\n--- Tambah Item Menu ---");
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        System.out.println("3. Diskon");
        System.out.println("0. Batal");
        int jenis = bacaAngka("Pilih jenis (0-3): ", 0, 3);
        if (jenis == 0) {
            return;
        }

        String nama = bacaNamaBaru("Nama item: ");

        try {
            MenuItem baru;
            if (jenis == 1) {
                double harga = bacaAngka("Harga (Rp, angka tanpa titik): ", 1, 10000000);
                String jenisMakanan = bacaTeks("Jenis makanan (mis. Nasi, Lauk, Sup): ");
                baru = new Makanan(nama, harga, jenisMakanan);
            } else if (jenis == 2) {
                double harga = bacaAngka("Harga (Rp, angka tanpa titik): ", 1, 10000000);
                String jenisMinuman = bacaTeks("Jenis minuman (mis. Dingin, Panas): ");
                baru = new Minuman(nama, harga, jenisMinuman);
            } else {
                double minimal = bacaAngka("Minimal belanja agar diskon berlaku (Rp): ", 0, 100000000);
                double persen = bacaAngka("Besar diskon (1-100 persen): ", 1, 100);
                baru = new Diskon(nama, minimal, persen);
            }
            menu.tambahItem(baru);
            System.out.println("Item '" + baru.getNama() + "' berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambah item: " + e.getMessage());
        }
    }

    static void simpanMenu() {
        try {
            menu.simpanKeFile(FILE_MENU);
            System.out.println("Menu berhasil disimpan ke " + FILE_MENU + ".");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu: " + e.getMessage());
        }
    }

    static void muatMenu() {
        try {
            int dilewati = menu.muatDariFile(FILE_MENU);
            System.out.println("Menu berhasil dimuat dari " + FILE_MENU
                    + " (" + menu.getJumlahItem() + " item).");
            if (dilewati > 0) {
                System.out.println(dilewati + " baris dilewati karena formatnya salah.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + FILE_MENU + " belum ada. Simpan menu dulu lewat pilihan 6.");
        } catch (IOException e) {
            System.out.println("Gagal memuat menu: " + e.getMessage());
        }
    }

    // ================== PESANAN ==================

    static void terimaPesanan() {
        if (!pesananAktif.isKosong()) {
            System.out.println("\nSudah ada pesanan aktif (" + pesananAktif.getJumlahItem() + " jenis item).");
            System.out.println("1. Tambah ke pesanan ini");
            System.out.println("2. Mulai pesanan baru");
            int pilih = bacaAngka("Pilih (1-2): ", 1, 2);
            if (pilih == 2) {
                pesananAktif = new Pesanan();
            }
        }

        menu.tampilkanMenu();
        System.out.println("\nKetik nomor menu untuk memesan. Ketik 0 jika sudah selesai.");

        boolean selesai = false;
        while (!selesai) {
            System.out.print("\nNomor menu (0 = selesai): ");
            String input = scanner.nextLine().trim();

            try {
                int nomor = Integer.parseInt(input);
                if (nomor == 0) {
                    selesai = true;
                } else {
                    MenuItem item = menu.getItem(nomor);
                    int jumlah = bacaAngka("Jumlah " + item.getNama() + ": ", 1, 99);
                    pesananAktif.tambahItem(item, jumlah);
                    System.out.println("Dicatat: " + jumlah + " x " + item.getNama());
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan nomor menu berupa angka.");
            } catch (ItemTidakDitemukanException e) {
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (pesananAktif.isKosong()) {
            System.out.println("Belum ada item yang dipesan.");
        } else {
            System.out.println("Pesanan dicatat. Pilih 4 untuk total biaya atau 5 untuk struk.");
        }
    }

    static void hitungTotal() {
        if (pesananAktif.isKosong()) {
            System.out.println("\nBelum ada pesanan. Pilih menu 3 untuk memesan.");
            return;
        }

        ArrayList<Diskon> promo = menu.getDaftarDiskon();
        Diskon diskon = pesananAktif.cariDiskonTerbaik(promo);

        System.out.println("\n--- Total Biaya Pesanan ---");
        System.out.println("Subtotal    : " + MenuItem.rp(pesananAktif.hitungSubtotal()));
        if (diskon != null) {
            System.out.println("Diskon      : " + diskon.getNama() + " (" + MenuItem.angka(diskon.getDiskon())
                    + "%) = - " + MenuItem.rp(pesananAktif.hitungNilaiDiskon(diskon)));
        } else {
            System.out.println("Diskon      : tidak ada diskon yang berlaku");
        }
        System.out.println("Total bayar : " + MenuItem.rp(pesananAktif.hitungTotal(promo)));
    }

    static void tampilkanStruk() {
        if (pesananAktif.isKosong()) {
            System.out.println("\nBelum ada pesanan. Pilih menu 3 untuk memesan.");
            return;
        }

        String struk = pesananAktif.buatStruk(menu.getDaftarDiskon());
        System.out.println();
        System.out.print(struk);

        if (konfirmasi("\nSimpan struk ke file " + pesananAktif.getNamaFileStruk() + "?")) {
            try {
                pesananAktif.simpanStruk(struk);
                System.out.println("Struk berhasil disimpan.");
            } catch (IOException e) {
                System.out.println("Gagal menyimpan struk: " + e.getMessage());
            }
        }
    }

    static void muatStruk() {
        ArrayList<String> daftarFile = Pesanan.daftarFileStruk();
        if (daftarFile.isEmpty()) {
            System.out.println("\nBelum ada file struk yang tersimpan.");
            return;
        }

        System.out.println("\n--- File Struk Tersimpan ---");
        for (int i = 0; i < daftarFile.size(); i++) {
            System.out.println((i + 1) + ". " + daftarFile.get(i));
        }
        int pilih = bacaAngka("Pilih file (0 untuk batal): ", 0, daftarFile.size());
        if (pilih == 0) {
            return;
        }

        try {
            System.out.println();
            System.out.print(Pesanan.bacaFileStruk(daftarFile.get(pilih - 1)));
        } catch (IOException e) {
            System.out.println("Gagal membaca struk: " + e.getMessage());
        }
    }

    // ================== INPUT ==================

    /** Meminta angka bulat pada rentang min-max; mengulang sampai valid. */
    static int bacaAngka(String prompt, int min, int max) {
        int angka = min;
        boolean valid = false;
        do {
            System.out.print(prompt);
            try {
                angka = Integer.parseInt(scanner.nextLine().trim());
                valid = angka >= min && angka <= max;
            } catch (NumberFormatException e) {
                valid = false;
            }
            if (!valid) {
                System.out.println("Input tidak valid. Masukkan angka bulat " + min + " sampai " + max + ".");
            }
        } while (!valid);
        return angka;
    }

    /** Meminta teks yang tidak kosong dan tidak memuat tanda ; */
    static String bacaTeks(String prompt) {
        String teks;
        boolean valid;
        do {
            System.out.print(prompt);
            teks = scanner.nextLine().trim();
            valid = !teks.isEmpty() && !teks.contains(";");
            if (!valid) {
                System.out.println("Input tidak boleh kosong dan tidak boleh memuat tanda ;");
            }
        } while (!valid);
        return teks;
    }

    /** Meminta nama item baru yang valid dan belum dipakai item lain. */
    static String bacaNamaBaru(String prompt) {
        String nama;
        boolean valid;
        do {
            nama = bacaTeks(prompt);
            valid = !menu.sudahAda(nama);
            if (!valid) {
                System.out.println("Nama '" + nama + "' sudah ada di menu, gunakan nama lain.");
            }
        } while (!valid);
        return nama;
    }

    /** Konfirmasi Ya/Tidak; mengulang sampai salah satunya diisi. */
    static boolean konfirmasi(String pertanyaan) {
        String jawab;
        do {
            System.out.print(pertanyaan + " (Ya/Tidak): ");
            jawab = scanner.nextLine().trim();
            if (!jawab.equalsIgnoreCase("Ya") && !jawab.equalsIgnoreCase("Tidak")) {
                System.out.println("Input tidak valid. Ketik 'Ya' atau 'Tidak'.");
            }
        } while (!jawab.equalsIgnoreCase("Ya") && !jawab.equalsIgnoreCase("Tidak"));
        return jawab.equalsIgnoreCase("Ya");
    }
}
