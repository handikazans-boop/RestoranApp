public class Minuman extends MenuItem {

public Minuman(String nama, double harga) {
super(nama, harga, kategori: "Minuman");
}
@override
public void tampilMenu() {
System.out.println(nama +" - Rp" + harga + " (Minuman)");
}
}
