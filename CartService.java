package ECOMMERCE_APPLICATION;

import java.util.*;
import java.io.*;

public class CartService {

    public static List<OrderItem> cart = new ArrayList<>();
    private static final String CART_FILE = "cart.csv";

    // Load cart from file at app startup
    public static void loadCart() {
        cart.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(CART_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                String itemId = d[0];
                int qty = Integer.parseInt(d[1]);

                Item item = Datamanager.products.get(itemId);
                if (item != null) {
                    cart.add(new OrderItem(item, qty));
                }
            }
        } catch (IOException e) {
            // First run: file may not exist
        }
    }

    // Save cart to file
    public static void saveCart() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CART_FILE))) {
            for (OrderItem oi : cart) {
                pw.println(oi.item.itemId + "," + oi.qty);
            }
        } catch (IOException e) {
            System.out.println("Error saving cart");
        }
    }

    public static void addToCart(String id, int qty) {
        Item item = Datamanager.products.get(id);

        if (item == null || qty <= 0 || qty > item.stock) {
            System.out.println("Invalid item or quantity");
            return;
        }

        cart.add(new OrderItem(item, qty));
        saveCart(); // 🔥 persist cart
        System.out.println("Item added to cart");
    }

    public static double calculateDiscount(double subtotal, String code) {
        Coupon c = Datamanager.coupons.get(code);
        if (c == null) return 0;

        if (c.type.equals("PERCENT"))
            return subtotal * c.value / 100;

        return Math.min(c.value, subtotal);
    }

    public static double calculateTax(double amt) {
        return amt * 0.18;
    }
}
