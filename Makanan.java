public class Makanan extends MenuItem {

public Makanan(String nama, double harga) {
super(nama, harga, kategori: "Makanan");

@override
public void tampilMenu() {
System.out.println(nama +" - Rp" + harga +" (Makanan)");

}
