import java.text.NumberFormat;
import java.util.Locale;

public abstract class MenuItem {

    private static final NumberFormat FORMAT_ANGKA =
            NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    static {
        FORMAT_ANGKA.setMaximumFractionDigits(2);
    }

    private String nama;
    private double harga;
    private final String kategori;

    protected MenuItem(String nama, double harga, String kategori) {
        this.nama = periksaTeks(nama, "Nama");
        this.harga = periksaHarga(harga);
        this.kategori = kategori;
    }

    // getter dan setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = periksaTeks(nama, "Nama");
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = periksaHarga(harga);
    }

    public String getKategori() {
        return kategori;
    }

    // method abstrak:

    /** Menampilkan informasi item menu ke layar. */
    public abstract void tampilMenu();

    /** Mengubah item menjadi satu baris teks untuk disimpan ke file. */
    public abstract String keBarisFile();

    // method bantu

    public static String angka(double nilai) {
        return FORMAT_ANGKA.format(nilai);
    }

    public static String rp(double nilai) {
        return "Rp " + angka(nilai);
    }

    /** Angka untuk file teks (selalu memakai titik sebagai desimal). */
    protected static String angkaFile(double nilai) {
        return String.format(Locale.ROOT, "%.2f", nilai);
    }

    /** Teks tidak boleh kosong dan tidak boleh memuat tanda ; (pemisah di file). */
    protected static String periksaTeks(String teks, String namaBidang) {
        if (teks == null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException(namaBidang + " tidak boleh kosong.");
        }
        if (teks.contains(";")) {
            throw new IllegalArgumentException(namaBidang + " tidak boleh memuat tanda ;");
        }
        return teks.trim();
    }

    private static double periksaHarga(double harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
        return harga;
    }
}
