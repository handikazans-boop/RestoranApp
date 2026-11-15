import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Mencatat pesanan seorang pelanggan, menghitung total, dan membuat struk. */
public class Pesanan {

    private static final String AWAL_NAMA_FILE = "struk_";
    private static final String GARIS = "---------------------------------------------";

    private final ArrayList<ItemPesanan> daftarPesanan = new ArrayList<>();
    private final LocalDateTime waktu = LocalDateTime.now();

    // mencatat pesanan

    /** Menambah item ke pesanan; item yang sama digabung jumlahnya. */
    public void tambahItem(MenuItem item, int jumlah) {
        if (item instanceof Diskon) {
            throw new IllegalArgumentException(
                    "Diskon tidak bisa dipesan, diskon diterapkan otomatis pada total.");
        }
        if (jumlah < 1) {
            throw new IllegalArgumentException("Jumlah pesanan minimal 1.");
        }
        for (ItemPesanan ip : daftarPesanan) {
            if (ip.getItem() == item) {
                ip.tambahJumlah(jumlah);
                return;
            }
        }
        daftarPesanan.add(new ItemPesanan(item, jumlah));
    }

    public boolean isKosong() {
        return daftarPesanan.isEmpty();
    }

    public int getJumlahItem() {
        return daftarPesanan.size();
    }

    // perhitungan

    public double hitungSubtotal() {
        double subtotal = 0;
        for (ItemPesanan ip : daftarPesanan) {
            subtotal += ip.getTotal();
        }
        return subtotal;
    }

    /** Mencari diskon dengan persentase terbesar yang syarat minimal belanjanya terpenuhi. */
    public Diskon cariDiskonTerbaik(List<Diskon> promo) {
        double subtotal = hitungSubtotal();
        Diskon terbaik = null;
        for (Diskon d : promo) {
            if (subtotal >= d.getMinimalBelanja()) {
                if (terbaik == null || d.getDiskon() > terbaik.getDiskon()) {
                    terbaik = d;
                }
            }
        }
        return terbaik;
    }

    public double hitungNilaiDiskon(Diskon diskon) {
        if (diskon == null) {
            return 0;
        }
        return hitungSubtotal() * diskon.getDiskon() / 100;
    }

    public double hitungTotal(List<Diskon> promo) {
        return hitungSubtotal() - hitungNilaiDiskon(cariDiskonTerbaik(promo));
    }

    // struk

    public String buatStruk(List<Diskon> promo) {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append("=============== STRUK PESANAN ===============").append(nl);
        sb.append("Tanggal : ")
                .append(waktu.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .append(nl);
        sb.append(GARIS).append(nl);

        int nomor = 1;
        for (ItemPesanan ip : daftarPesanan) {
            MenuItem item = ip.getItem();
            sb.append(nomor).append(". ").append(item.getNama())
                    .append(" (").append(item.getKategori()).append(")").append(nl);
            sb.append("   ").append(ip.getJumlah()).append(" x ")
                    .append(MenuItem.rp(item.getHarga())).append(" = ")
                    .append(MenuItem.rp(ip.getTotal())).append(nl);
            nomor++;
        }

        sb.append(GARIS).append(nl);
        sb.append(baris("Subtotal", MenuItem.rp(hitungSubtotal()))).append(nl);

        Diskon diskon = cariDiskonTerbaik(promo);
        if (diskon != null) {
            sb.append(baris("Diskon " + diskon.getNama() + " (" + MenuItem.angka(diskon.getDiskon()) + "%)",
                    "- " + MenuItem.rp(hitungNilaiDiskon(diskon)))).append(nl);
        }

        sb.append(GARIS).append(nl);
        sb.append(baris("TOTAL BAYAR", MenuItem.rp(hitungTotal(promo)))).append(nl);
        sb.append("=============================================").append(nl);
        return sb.toString();
    }

    private String baris(String label, String nilai) {
        return String.format("%-31s: %13s", label, nilai);
    }

    // operasi file struk
  
    public String getNamaFileStruk() {
        return AWAL_NAMA_FILE + waktu.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
    }

    public void simpanStruk(String struk) throws IOException {
        try (BufferedWriter penulis = new BufferedWriter(new FileWriter(getNamaFileStruk()))) {
            penulis.write(struk);
        }
    }

    /** Daftar nama file struk yang ada di folder program, urut dari yang terlama. */
    public static ArrayList<String> daftarFileStruk() {
        ArrayList<String> hasil = new ArrayList<>();
        String[] semua = new File(".").list();
        if (semua != null) {
            for (String nama : semua) {
                if (nama.startsWith(AWAL_NAMA_FILE) && nama.endsWith(".txt")) {
                    hasil.add(nama);
                }
            }
        }
        Collections.sort(hasil);
        return hasil;
    }

    public static String bacaFileStruk(String namaFile) throws IOException {
        StringBuilder isi = new StringBuilder();
        try (BufferedReader pembaca = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = pembaca.readLine()) != null) {
                isi.append(baris).append(System.lineSeparator());
            }
        }
        return isi.toString();
    }
}
