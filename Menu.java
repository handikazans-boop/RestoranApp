import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    private static final String[] URUTAN_KATEGORI = {"Makanan", "Minuman", "Diskon"};

    private ArrayList<MenuItem> daftarItem = new ArrayList<>();

    //pengelolaan item

    public void tambahItem(MenuItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item tidak boleh null.");
        }
        if (adaNama(daftarItem, item.getNama())) {
            throw new IllegalArgumentException("Item dengan nama '" + item.getNama() + "' sudah ada.");
        }
        daftarItem.add(item);
    }

    /** Mengambil item berdasarkan nomor tampilan (mulai dari 1). */
    public MenuItem getItem(int nomor) throws ItemTidakDitemukanException {
        if (nomor < 1 || nomor > daftarItem.size()) {
            throw new ItemTidakDitemukanException(nomor);
        }
        return daftarItem.get(nomor - 1);
    }

    public int getJumlahItem() {
        return daftarItem.size();
    }

    public boolean sudahAda(String nama) {
        return adaNama(daftarItem, nama);
    }

    public ArrayList<Diskon> getDaftarDiskon() {
        ArrayList<Diskon> hasil = new ArrayList<>();
        for (MenuItem item : daftarItem) {
            if (item instanceof Diskon) {
                hasil.add((Diskon) item);
            }
        }
        return hasil;
    }

    private boolean adaNama(List<MenuItem> daftar, String nama) {
        for (MenuItem item : daftar) {
            if (item.getNama().equalsIgnoreCase(nama.trim())) {
                return true;
            }
        }
        return false;
    }


    public void tampilkanMenu() {
        for (String kategori : URUTAN_KATEGORI) {
            System.out.println("\n===== " + kategori.toUpperCase() + " =====");
            boolean ada = false;
            for (int i = 0; i < daftarItem.size(); i++) {
                MenuItem item = daftarItem.get(i);
                if (item.getKategori().equals(kategori)) {
                    System.out.print((i + 1) + ". ");
                    item.tampilMenu();
                    ada = true;
                }
            }
            if (!ada) {
                System.out.println("(belum ada)");
            }
        }
    }

    //operasi file

    public void simpanKeFile(String namaFile) throws IOException {
        try (BufferedWriter penulis = new BufferedWriter(new FileWriter(namaFile))) {
            for (MenuItem item : daftarItem) {
                penulis.write(item.keBarisFile());
                penulis.newLine();
            }
        }
    }

    public int muatDariFile(String namaFile) throws IOException {
        ArrayList<MenuItem> sementara = new ArrayList<>();
        int dilewati = 0;

        try (BufferedReader pembaca = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = pembaca.readLine()) != null) {
                if (baris.trim().isEmpty()) {
                    continue;
                }
                try {
                    MenuItem item = buatDariBaris(baris);
                    if (adaNama(sementara, item.getNama())) {
                        throw new IllegalArgumentException("Nama kembar.");
                    }
                    sementara.add(item);
                } catch (IllegalArgumentException e) {
                    dilewati++;
                }
            }
        }

        if (sementara.isEmpty()) {
            throw new IOException("File tidak berisi data menu yang valid.");
        }
        daftarItem = sementara;
        return dilewati;
    }

    private static MenuItem buatDariBaris(String baris) {
        String[] bagian = baris.split(";", -1);
        if (bagian.length != 4) {
            throw new IllegalArgumentException("Format baris salah.");
        }
        String jenis = bagian[0].trim();
        String nama = bagian[1].trim();
        double angka = Double.parseDouble(bagian[2].trim());

        switch (jenis) {
            case "Makanan":
                return new Makanan(nama, angka, bagian[3].trim());
            case "Minuman":
                return new Minuman(nama, angka, bagian[3].trim());
            case "Diskon":
                return new Diskon(nama, angka, Double.parseDouble(bagian[3].trim()));
            default:
                throw new IllegalArgumentException("Jenis tidak dikenal: " + jenis);
        }
    }

    //data awal

    public void isiDataAwal() {
        daftarItem.clear();
        tambahItem(new Makanan("Nasi Katsu", 25000, "Nasi"));
        tambahItem(new Makanan("Nasi Goreng", 20000, "Nasi"));
        tambahItem(new Makanan("Ayam Bakar", 30000, "Lauk"));
        tambahItem(new Makanan("Rawon", 40000, "Sup"));
        tambahItem(new Minuman("Lychee Tea", 12000, "Dingin"));
        tambahItem(new Minuman("Americano", 18000, "Panas"));
        tambahItem(new Minuman("Matcha Latte", 18000, "Dingin"));
        tambahItem(new Minuman("Aren Latte", 22000, "Dingin"));
        tambahItem(new Diskon("Promo Hemat", 50000, 5));
        tambahItem(new Diskon("Promo Super Hemat", 100000, 10));
    }
}
