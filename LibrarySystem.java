import java.util.Scanner;

// Custom checked exception for library errors
class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

// Stores book information and controls borrowing and returning
class Book {
    private final int id;
    private final String title;
    private final int totalCopies;
    private int available;
    private int borrowed;

    public Book(int id, String title, int copies) {
        this.id = id;
        this.title = title;
        this.totalCopies = copies;
        this.available = copies;
        this.borrowed = 0;
    }

    public int getId() {
        return id;
    }

    public void borrow() throws LibraryException {
        if (available == 0) {
            throw new LibraryException("No copies available.");
        }
        available--;
        borrowed++;
    }

    public void returnCopy() throws LibraryException {
        if (borrowed == 0) {
            throw new LibraryException("No borrowed copies to return.");
        }
        available++;
        borrowed--;
    }

    public void display() {
        System.out.println("ID " + id + " - " + title
                + " | Available " + available
                + ", borrowed " + borrowed);
    }
}

public class LibrarySystem {
    // Find a book in the required array of objects
    public static Book findBook(Book[] books, int id) throws LibraryException {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        throw new LibraryException("Book not found.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Book[] books = {
            new Book(1, "Java Basics", 2),
            new Book(2, "Algorithms", 1),
            new Book(3, "Databases", 0)
        };
        int choice = -1;

        do {
            System.out.println("\n=== LIBRARY BORROWING SYSTEM ===");
            System.out.println("1 Borrow | 2 Return | 3 Find | 4 List | 0 Exit");
            System.out.print("Choose an option: ");

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice == 0) {
                    System.out.println("Thank you for using the library system!");
                } else if (choice >= 1 && choice <= 4) {
                    if (choice == 4) {
                        System.out.println("\n--- BOOK LIST ---");
                        for (Book book : books) {
                            book.display();
                        }
                    } else {
                        System.out.print("Enter book ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        Book book = findBook(books, id);
                        switch (choice) {
                            case 1:
                                book.borrow();
                                System.out.println("Book borrowed successfully.");
                                break;
                            case 2:
                                book.returnCopy();
                                System.out.println("Book returned successfully.");
                                break;
                            case 3:
                                System.out.println("\n--- BOOK DETAILS ---");
                                break;
                        }
                        book.display();
                    }
                } else {
                    System.out.println("Error: Invalid menu option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Enter a valid number.");
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("Operation completed.");
            }
        } while (choice != 0);

        sc.close();
    }
}
