package ECOMMERCE_APPLICATION;

public class OrderItem {
    public Item item;
    public int qty;

    public OrderItem(Item item, int qty) {
        this.item = item;
        this.qty = qty;
    }

    public double getSubtotal() {
        return item.price * qty;
    }
}
