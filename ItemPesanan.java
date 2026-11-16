public class ItemPesanan {

    private final MenuItem item;
    private int jumlah;

    public ItemPesanan(MenuItem item, int jumlah) {
        this.item = item;
        this.jumlah = jumlah;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void tambahJumlah(int tambahan) {
        this.jumlah += tambahan;
    }

    public double getTotal() {
        return item.getHarga() * jumlah;
    }
}
