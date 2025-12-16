package ECOMMERCE_APPLICATION;

import java.util.*;
import java.io.*;

public class EcommerceApp {

    public static void main(String[] args) {
        System.out.println("✅ Starting E-Commerce System...");
        Datamanager.loadData();
        System.out.println("✅ Data loaded!");
        runMenu();
    }

    private static void runMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int cartCount = CartService.cart.size();

            System.out.println();
            System.out.println("==============================");
            System.out.println("       E-COMMERCE SYSTEM");
            System.out.println("==============================");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart (" + cartCount + ")");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice, try again.");
                continue;
            }

            switch (ch) {
                case 1 -> showProducts();
                case 2 -> addToCartFlow(sc);
                case 3 -> showCart();
                case 4 -> checkout(sc);
                case 5 -> {
                    System.out.println("Thank you for using E-Commerce System!");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void showProducts() {
        System.out.println();
        System.out.println("--------- PRODUCT LIST ---------");
        for (Object o : Datamanager.products.values()) {
            Item i = (Item) o;
            System.out.printf("ID: %-5s  %-15s  ₹%.2f  Stock: %d  [%s]%n",
                    i.itemId, i.name, i.price, i.stock, i.category);
        }
    }

    private static void addToCartFlow(Scanner sc) {
        System.out.print("Enter Product ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Quantity : ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid quantity.");
            return;
        }
        CartService.addToCart(id, qty);               // uses CartService.addToCart[file:2]
    }

    private static void showCart() {
        System.out.println();
        if (CartService.cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("------------- CART -------------");
        double subtotal = 0;
        for (OrderItem oi : CartService.cart) {       // items in cart[file:2][file:3]
            double itemTotal = oi.getSubtotal();      // price * qty[file:3]
            subtotal += itemTotal;
            System.out.printf("%-15s x %d  =  ₹%.2f%n",
                    oi.item.name, oi.qty, itemTotal);
        }
        System.out.printf("Subtotal: ₹%.2f%n", subtotal);
    }

    private static void checkout(Scanner sc) {
        if (CartService.cart.isEmpty()) {
            System.out.println("Cart is empty. Add items first.");
            return;
        }

        // compute subtotal
        double subtotal = 0;
        for (OrderItem oi : CartService.cart) {
            subtotal += oi.getSubtotal();
        }

        // coupon
        System.out.print("Enter coupon code (or press Enter to skip): ");
        String code = sc.nextLine().trim();
        double discount = code.isEmpty()
                ? 0
                : CartService.calculateDiscount(subtotal, code); // discount logic[file:2]

        double afterDiscount = subtotal - discount;
        double tax = CartService.calculateTax(afterDiscount);    // tax logic[file:2]
        double total = afterDiscount + tax;

        // ---------- INVOICE ON CONSOLE ----------
        System.out.println();
        System.out.println("=========== INVOICE ===========");
        System.out.printf("%-15s %-5s %-8s %-8s%n", "Item", "Qty", "Price", "Total");
        System.out.println("----------------------------------------");

        for (OrderItem oi : CartService.cart) {
            double lineTotal = oi.getSubtotal();
            System.out.printf("%-15s %-5d %-8.2f %-8.2f%n",
                    oi.item.name, oi.qty, oi.item.price, lineTotal);
        }

        System.out.println("----------------------------------------");
        System.out.printf("Subtotal : %.2f%n", subtotal);
        System.out.printf("Discount : -%.2f%n", discount);
        System.out.printf("Tax      : %.2f%n", tax);
        System.out.printf("TOTAL    : %.2f%n", total);
        System.out.println("===============================");

        // ---------- INVOICE TO FILE ----------
        try (PrintWriter pw = new PrintWriter(new FileWriter("invoice.txt"))) {
            pw.println("=========== INVOICE ===========");
            pw.printf("%-15s %-5s %-8s %-8s%n", "Item", "Qty", "Price", "Total");
            pw.println("----------------------------------------");

            for (OrderItem oi : CartService.cart) {
                double lineTotal = oi.getSubtotal();
                pw.printf("%-15s %-5d %-8.2f %-8.2f%n",
                        oi.item.name, oi.qty, oi.item.price, lineTotal);
            }

            pw.println("----------------------------------------");
            pw.printf("Subtotal : %.2f%n", subtotal);
            pw.printf("Discount : -%.2f%n", discount);
            pw.printf("Tax      : %.2f%n", tax);
            pw.printf("TOTAL    : %.2f%n", total);
            pw.println("===============================");

            System.out.println("Invoice saved to invoice.txt");
        } catch (IOException e) {
            System.out.println("Error writing invoice file: " + e.getMessage());
        }

        // optional: clear cart after checkout
        CartService.cart.clear();
    }
}
