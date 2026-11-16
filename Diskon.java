public class Diskon extends MenuItem {

    private double diskon; // dalam persen, 0 < diskon <= 100

    public Diskon(String nama, double minimalBelanja, double diskon) {
        super(nama, minimalBelanja, "Diskon");
        this.diskon = periksaDiskon(diskon);
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        this.diskon = periksaDiskon(diskon);
    }

    public double getMinimalBelanja() {
        return getHarga();
    }

    private static double periksaDiskon(double diskon) {
        if (diskon <= 0 || diskon > 100) {
            throw new IllegalArgumentException("Diskon harus lebih dari 0 dan paling besar 100 persen.");
        }
        return diskon;
    }

    @Override
    public void tampilMenu() {
        System.out.println(getNama() + " - Diskon " + angka(diskon)
                + "% untuk belanja minimal " + rp(getMinimalBelanja()));
    }

    @Override
    public String keBarisFile() {
        return "Diskon;" + getNama() + ";" + angkaFile(getHarga()) + ";" + angkaFile(diskon);
    }
}
