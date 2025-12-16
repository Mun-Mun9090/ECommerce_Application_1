package ECOMMERCE_APPLICATION;

import java.util.*;
import java.io.*;

public class EcommerceApp {

    public static void main(String[] args) {
        System.out.println("✅ Starting E-Commerce System...");
        Datamanager.loadData();
        CartService.loadCart();   //  RESTORE CART
        System.out.println("✅ Data loaded!");
        runMenu();
    }

    private static void runMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("       E-COMMERCE SYSTEM");
            System.out.println("==============================");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart (" + CartService.cart.size() + ")");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice.");
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
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void showProducts() {
        System.out.println("\n--------- PRODUCT LIST ---------");
        for (Item i : Datamanager.products.values()) {
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

        CartService.addToCart(id, qty);
    }

    private static void showCart() {
        if (CartService.cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n------------- CART -------------");
        double subtotal = 0;

        for (OrderItem oi : CartService.cart) {
            double total = oi.getSubtotal();
            subtotal += total;
            System.out.printf("%-15s x %d  = ₹%.2f%n",
                    oi.item.name, oi.qty, total);
        }

        System.out.printf("Subtotal: ₹%.2f%n", subtotal);
    }

    private static void checkout(Scanner sc) {
        if (CartService.cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double subtotal = 0;
        for (OrderItem oi : CartService.cart)
            subtotal += oi.getSubtotal();

        System.out.print("Enter coupon code (or press Enter to skip): ");
        String code = sc.nextLine().trim();

        double discount = code.isEmpty()
                ? 0
                : CartService.calculateDiscount(subtotal, code);

        double taxable = subtotal - discount;
        double tax = CartService.calculateTax(taxable);
        double total = taxable + tax;

        System.out.println("\n=========== INVOICE ===========");
        for (OrderItem oi : CartService.cart) {
            System.out.printf("%-15s x %d = ₹%.2f%n",
                    oi.item.name, oi.qty, oi.getSubtotal());
        }

        System.out.println("------------------------------");
        System.out.printf("Subtotal : %.2f%n", subtotal);
        System.out.printf("Discount : -%.2f%n", discount);
        System.out.printf("Tax      : %.2f%n", tax);
        System.out.printf("TOTAL    : %.2f%n", total);

        // Save invoice
        try (PrintWriter pw = new PrintWriter(new FileWriter("invoice.txt"))) {
            pw.println("TOTAL : " + total);
        } catch (IOException e) {
            System.out.println("Invoice file error");
        }

        // Clear cart after checkout
        CartService.cart.clear();
        CartService.saveCart();  // 🔥 clear cart file too
    }
}
