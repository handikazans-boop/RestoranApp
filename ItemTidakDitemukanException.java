public class ItemTidakDitemukanException extends Exception {

    private static final long serialVersionUID = 1L;

    public ItemTidakDitemukanException(int nomor) {
        super("Item nomor " + nomor + " tidak ada di menu.");
    }
}
