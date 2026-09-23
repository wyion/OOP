package exer2;
import java.util.Scanner;

class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean reduceStock(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            return false;
        }

        stock -= quantity;
        return true;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getCode() {
        return product.getCode();
    }

    public String getName() {
        return product.getName();
    }

    public double getPrice() {
        return product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}

class ShoppingCart {
    private CartItem[] items;
    private int itemCount;

    public ShoppingCart() {
        items = new CartItem[100];
        itemCount = 0;
    }

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Addition rejected: quantity must be positive.");
            return;
        }

        if (quantity > product.getStock()) {
            System.out.println("Addition rejected: insufficient stock.");
            return;
        }

        for (int i = 0; i < itemCount; i++) {
            if (items[i].getCode().equals(product.getCode())) {
                if (product.reduceStock(quantity)) {
                    items[i].addQuantity(quantity);
                    System.out.println("Added " + quantity + " more "
                            + product.getName() + " to cart.");
                }
                return;
            }
        }

        if (itemCount >= items.length) {
            System.out.println("Addition rejected: cart is full.");
            return;
        }

        if (product.reduceStock(quantity)) {
            items[itemCount] = new CartItem(product, quantity);
            itemCount++;

            System.out.println("Added " + quantity + " "
                    + product.getName() + " to cart.");
        }
    }

    public double getSubtotal() {
        double subtotal = 0;

        for (int i = 0; i < itemCount; i++) {
            subtotal += items[i].getLineTotal();
        }

        return subtotal;
    }

    public double getDiscount() {
        double subtotal = getSubtotal();

        if (subtotal >= 5000) {
            return subtotal * 0.10;
        } else if (subtotal >= 2000) {
            return subtotal * 0.05;
        } else {
            return 0;
        }
    }

    public double getVAT() {
        double subtotal = getSubtotal();
        double discount = getDiscount();
        double discountedAmount = subtotal - discount;

        return discountedAmount * 0.12;
    }

    public double getFinalTotal() {
        double subtotal = getSubtotal();
        double discount = getDiscount();
        double vat = getVAT();

        return subtotal - discount + vat;
    }

    public void displayReceipt() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("                 RECEIPT");
        System.out.println("==============================================");

        if (itemCount == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.printf("%-10s %-15s %-8s %-12s %-12s%n",
                "Code", "Product", "Qty", "Price", "Total");

        System.out.println("----------------------------------------------");

        for (int i = 0; i < itemCount; i++) {
            System.out.printf("%-10s %-15s %-8d %-12.2f %-12.2f%n",
                    items[i].getCode(),
                    items[i].getName(),
                    items[i].getQuantity(),
                    items[i].getPrice(),
                    items[i].getLineTotal());
        }

        System.out.println("----------------------------------------------");

        System.out.printf("Subtotal:       %.2f%n", getSubtotal());
        System.out.printf("Discount:       %.2f%n", getDiscount());
        System.out.printf("VAT (12%%):      %.2f%n", getVAT());
        System.out.printf("Final Total:    %.2f%n", getFinalTotal());
        System.out.println("==============================================");
    }
}

public class mp8 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Product[] catalog = {
            new Product("P1", "Mouse", 500, 10),
            new Product("P2", "Keyboard", 2500, 5),
            new Product("P3", "Cable", 100, 2)
        };

        int catalogSize = catalog.length;
        ShoppingCart cart = new ShoppingCart();

        System.out.println("===== SHOPPING CART SYSTEM =====");

        System.out.print("Enter number of add-to-cart actions: ");
        int actions = input.nextInt();

        for (int i = 0; i < actions; i++) {
            System.out.println();
            System.out.println("Action " + (i + 1));

            System.out.print("Enter product code: ");
            String code = input.next();

            System.out.print("Enter quantity: ");
            int quantity = input.nextInt();

            boolean found = false;

            for (int j = 0; j < catalogSize; j++) {
                if (catalog[j].getCode().equals(code)) {
                    cart.addItem(catalog[j], quantity);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Addition rejected: product not found.");
            }
        }

        cart.displayReceipt();

        System.out.println();
        System.out.println("===== REMAINING CATALOG STOCK =====");

        for (int i = 0; i < catalogSize; i++) {
            System.out.println(catalog[i].getCode() + " - "
                    + catalog[i].getName() + ": "
                    + catalog[i].getStock() + " remaining");
        }

        input.close();
    }
}
