package exercise2;
import java.util.Scanner;
public class inventorymonitor {
   static class Product {
       private String code;
       private String name;
       private double price;
       private int stock;
       private static int count = 0;

       Product(String code, String name, double price, int stock) {
           this.code = code;
           this.name = name;
           this.price = price;
           this.stock = stock;
           count++;
       }
       String getCode() {
           return code;
       }
       String getName() {
           return name;
       }
       double getPrice() {
           return price;
       }
       int getStock() {
           return stock;
       }
       void restock(int quantity) {
           if (quantity > 0) {
               stock += quantity;
               System.out.println("Restock successful.");
           } else {
               System.out.println("Invalid quantity.");
           }
       }
       boolean sell(int quantity) {
           if (quantity > 0 && quantity <= stock) {
               stock -= quantity;
               return true;
           }
           return false;
       }
       double getValue() {
           return price * stock;
       }
   }
   public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
       System.out.print("Enter number of products (1-8): ");
       int numberOfProducts = input.nextInt();
       if (numberOfProducts < 1 || numberOfProducts > 8) {
           System.out.println("Number of products must be from 1 to 8.");
           return;
       }
       Product[] products = new Product[numberOfProducts];
       for (int i = 0; i < numberOfProducts; i++) {
           System.out.println("\nProduct " + (i + 1));
           System.out.print("Code: ");
           String code = input.next();
           System.out.print("Name: ");
           String name = input.next();
           System.out.print("Price: ");
           double price = input.nextDouble();
           System.out.print("Opening stock: ");
           int stock = input.nextInt();
           if (price < 0 || stock < 0) {
               System.out.println("Price and stock cannot be negative.");
               i--;
               continue;
           }
           products[i] = new Product(code, name, price, stock);
       }
       System.out.print("\nEnter number of transactions: ");
       int transactions = input.nextInt();
       for (int i = 0; i < transactions; i++) {
           System.out.println("\nTransaction " + (i + 1));
           System.out.print("Product code: ");
           String code = input.next();
           Product product = null;
           for (int j = 0; j < products.length; j++) {
               if (products[j].getCode().equalsIgnoreCase(code)) {
                   product = products[j];
                   break;
               }
           }
           if (product == null) {
               System.out.println("Product not found.");
               continue;
           }
           System.out.print("R = Restock, S = Sell: ");
           char type = input.next().toUpperCase().charAt(0);
           System.out.print("Quantity: ");
           int quantity = input.nextInt();
           if (type == 'R') {
               product.restock(quantity);
           } else if (type == 'S') {
               if (product.sell(quantity)) {
                   System.out.println("Sale successful.");
               } else {
                   System.out.println("Sale rejected.");
               }
           } else {
               System.out.println("Invalid transaction.");
           }
       }
       System.out.println("\n--- INVENTORY SUMMARY ---");
       double totalValue = 0;
       for (int i = 0; i < products.length; i++) {
           Product p = products[i];
           System.out.println("\nCode: " + p.getCode());
           System.out.println("Name: " + p.getName());
           System.out.println("Stock: " + p.getStock());
           System.out.println("Inventory Value: PHP " + p.getValue());
           if (p.getStock() <= 5) {
               System.out.println("LOW STOCK");
           }
           totalValue += p.getValue();
       }
       System.out.println("\nTotal Inventory Value: PHP " + totalValue);
       System.out.println("Products Created: " + Product.count);
       input.close();
   }
}