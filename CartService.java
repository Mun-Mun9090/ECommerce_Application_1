package ECOMMERCE_APPLICATION;
import ECOMMERCE_APPLICATION.OrderItem;
import java.util.*;

public class CartService {

    public static List<OrderItem> cart = new ArrayList<>();

    public static void addToCart(String id, int qty) {
        Item item = Datamanager.products.get(id);
        if (item == null || qty <= 0 || qty > item.stock) {
            System.out.println("Invalid item or quantity");
            return;
        }
        cart.add(new OrderItem(item, qty));
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
