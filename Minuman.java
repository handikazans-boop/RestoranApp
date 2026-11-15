public class Minuman extends MenuItem {

    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = periksaTeks(jenisMinuman, "Jenis minuman");
    }

    public String getJenisMinuman() {
        return jenisMinuman;
    }

    public void setJenisMinuman(String jenisMinuman) {
        this.jenisMinuman = periksaTeks(jenisMinuman, "Jenis minuman");
    }

    @Override
    public void tampilMenu() {
        System.out.println(getNama() + " - " + rp(getHarga())
                + "  [Jenis: " + jenisMinuman + "]");
    }

    @Override
    public String keBarisFile() {
        return "Minuman;" + getNama() + ";" + angkaFile(getHarga()) + ";" + jenisMinuman;
    }
}
