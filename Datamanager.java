package ECOMMERCE_APPLICATION;

import java.io.*;
import java.util.*;

public class Datamanager {

    public static Map<String, Item> products = new HashMap<>();
    public static Map<String, Coupon> coupons = new HashMap<>();

    public static void loadData() {
        loadProducts();
        loadCoupons();
    }

    private static void loadProducts() {
        try (BufferedReader br = new BufferedReader(new FileReader("products.csv"))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                Item item = new Item(d[0], d[1],
                        Double.parseDouble(d[2]),
                        Integer.parseInt(d[3]),
                        d[4]);
                products.put(item.itemId, item);
            }
        } catch (Exception e) {
            System.out.println("Products file error");
        }
    }

    private static void loadCoupons() {
        try (BufferedReader br = new BufferedReader(new FileReader("coupons.csv"))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                coupons.put(d[0], new Coupon(d[0], d[1],
                        Double.parseDouble(d[2])));
            }
        } catch (Exception e) {
            System.out.println("Coupons file error");
        }
    }
}
