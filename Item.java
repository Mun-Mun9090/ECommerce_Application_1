package ECOMMERCE_APPLICATION;

public class Item {
    public String itemId;
    public String name;
    public double price;
    public int stock;
    public String category;

    public Item(String itemId, String name, double price, int stock, String category) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}
