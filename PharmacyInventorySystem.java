import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

interface InventoryOperations {
    void addMedicine(Medicine medicine);
    void displayMedicines();
    Medicine findMedicine(String id);
    boolean removeMedicine(String id);
}

class Medicine {
    private String id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private LocalDate expiryDate;

    public Medicine(String id, String name, String category, int quantity, double price, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public void display() {
        System.out.printf("%-10s %-22s %-15s %-8d %-12.2f %-12s%n",
                id, name, category, quantity, price, expiryDate);
    }
}

class PharmacyInventory implements InventoryOperations {
    private ArrayList<Medicine> medicines = new ArrayList<>();
    private static final int LOW_STOCK_LIMIT = 10;

    public void addMedicine(Medicine medicine) { medicines.add(medicine); }
    public Medicine findMedicine(String id) {
        for (Medicine medicine : medicines) {
            if (medicine.getId().equalsIgnoreCase(id)) return medicine;
        }
        return null;
    }
    public boolean removeMedicine(String id) {
        Medicine medicine = findMedicine(id);
        return medicine != null && medicines.remove(medicine);
    }
    public void displayMedicines() {
        if (medicines.isEmpty()) { System.out.println("Inventory is empty."); return; }
        printHeader();
        for (Medicine medicine : medicines) medicine.display();
    }
    public void showLowStock() {
        boolean found = false;
        for (Medicine medicine : medicines) {
            if (medicine.getQuantity() <= LOW_STOCK_LIMIT) {
                if (!found) printHeader();
                medicine.display(); found = true;
            }
        }
        if (!found) System.out.println("No low-stock medicines.");
    }
    public void showExpired() {
        boolean found = false;
        for (Medicine medicine : medicines) {
            if (!medicine.getExpiryDate().isAfter(LocalDate.now())) {
                if (!found) printHeader();
                medicine.display(); found = true;
            }
        }
        if (!found) System.out.println("No expired or expiring-today medicines.");
    }
    private void printHeader() {
        System.out.printf("%-10s %-22s %-15s %-8s %-12s %-12s%n",
                "ID", "Name", "Category", "Stock", "Price (PHP)", "Expiry");
        System.out.println("-----------------------------------------------------------------------------------");
    }
}

public class PharmacyInventorySystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PharmacyInventory inventory = new PharmacyInventory();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== PHARMACY INVENTORY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add medicine");
            System.out.println("2. View all medicines");
            System.out.println("3. Search medicine");
            System.out.println("4. Update medicine");
            System.out.println("5. Remove medicine");
            System.out.println("6. View low-stock medicines (10 or fewer)");
            System.out.println("7. View expired medicines");
            System.out.println("8. Exit");
            int choice = readInt("Choose an option: ", 1, 8);
            switch (choice) {
                case 1: addMedicine(); break;
                case 2: inventory.displayMedicines(); break;
                case 3: searchMedicine(); break;
                case 4: updateMedicine(); break;
                case 5: removeMedicine(); break;
                case 6: inventory.showLowStock(); break;
                case 7: inventory.showExpired(); break;
                case 8: System.out.println("Thank you for using the system!"); return;
            }
        }
    }

    private static void addMedicine() {
        String id = readText("Medicine ID: ");
        if (inventory.findMedicine(id) != null) {
            System.out.println("That medicine ID already exists."); return;
        }
        String name = readText("Medicine name: ");
        String category = readCategory();
        int quantity = readInt("Quantity: ", 0, Integer.MAX_VALUE);
        double price = readPrice();
        LocalDate expiry = readDate("Expiration date (YYYY-MM-DD): ");
        inventory.addMedicine(new Medicine(id, name, category, quantity, price, expiry));
        System.out.println("Medicine added successfully.");
    }

    private static void searchMedicine() {
        Medicine medicine = inventory.findMedicine(readText("Enter medicine ID: "));
        if (medicine == null) System.out.println("Medicine not found.");
        else medicine.display();
    }

    private static void updateMedicine() {
        Medicine medicine = inventory.findMedicine(readText("Enter medicine ID to update: "));
        if (medicine == null) { System.out.println("Medicine not found."); return; }
        System.out.println("1. Name  2. Category  3. Quantity  4. Price  5. Expiration date");
        int field = readInt("Choose field: ", 1, 5);
        switch (field) {
            case 1: medicine.setName(readText("New name: ")); break;
            case 2: medicine.setCategory(readCategory()); break;
            case 3: medicine.setQuantity(readInt("New quantity: ", 0, Integer.MAX_VALUE)); break;
            case 4: medicine.setPrice(readPrice()); break;
            case 5: medicine.setExpiryDate(readDate("New expiration date (YYYY-MM-DD): ")); break;
        }
        System.out.println("Medicine updated successfully.");
    }

    private static void removeMedicine() {
        String id = readText("Enter medicine ID to remove: ");
        Medicine medicine = inventory.findMedicine(id);
        if (medicine == null) { System.out.println("Medicine not found."); return; }
        String confirm = readText("Confirm removal of " + medicine.getName() + "? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            inventory.removeMedicine(id);
            System.out.println("Medicine removed successfully.");
        } else System.out.println("Removal cancelled.");
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty.");
        }
    }
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readText(prompt));
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a valid whole number from " + min + " to " + max + ".");
        }
    }
    private static double readPrice() {
        while (true) {
            try {
                double value = Double.parseDouble(readText("Price (PHP): "));
                if (Double.isFinite(value) && value >= 0) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a valid nonnegative price.");
        }
    }
    private static LocalDate readDate(String prompt) {
        while (true) {
            try { return LocalDate.parse(readText(prompt)); }
            catch (DateTimeParseException ex) { System.out.println("Use a valid date in YYYY-MM-DD format."); }
        }
    }
    private static String readCategory() {
        System.out.println("1. Over-the-Counter (OTC)  2. Prescription");
        return readInt("Choose category: ", 1, 2) == 1 ? "OTC" : "Prescription";
    }
}
