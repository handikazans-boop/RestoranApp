public class Makanan extends MenuItem {

    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = periksaTeks(jenisMakanan, "Jenis makanan");
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    public void setJenisMakanan(String jenisMakanan) {
        this.jenisMakanan = periksaTeks(jenisMakanan, "Jenis makanan");
    }

    @Override
    public void tampilMenu() {
        System.out.println(getNama() + " - " + rp(getHarga())
                + "  [Jenis: " + jenisMakanan + "]");
    }

    @Override
    public String keBarisFile() {
        return "Makanan;" + getNama() + ";" + angkaFile(getHarga()) + ";" + jenisMakanan;
    }
}
