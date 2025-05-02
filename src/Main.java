import models.*;
import services.LibraryService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        Scanner scanner = new Scanner(System.in);
        
        initializeSampleData(library);
        
        while (true) {
            try {
                System.out.println("\n==== Library System ====");
                System.out.println("1. Add Book");
                System.out.println("2. Register Borrower");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Search Books");
                System.out.println("6. View Overdue Books");
                System.out.println("7. View Borrower History");
                System.out.println("8. Exit");
                System.out.print("Choose option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                
                switch (choice) {
                    case 1 -> addBookMenu(library, scanner);
                    case 2 -> registerBorrowerMenu(library, scanner);
                    case 3 -> borrowBookMenu(library, scanner);
                    case 4 -> returnBookMenu(library, scanner);
                    case 5 -> searchBooksMenu(library, scanner);
                    case 6 -> viewOverdueBooksMenu(library);
                    case 7 -> viewBorrowerHistoryMenu(library, scanner);
                    case 8 -> { 
                        System.out.println("Exiting..."); 
                        scanner.close(); 
                        System.exit(0); 
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 8.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear any remaining input
            }
        }
    }

    private static void initializeSampleData(LibraryService lib) {
        try {
            lib.addBook(new PaperBook("Clean Code", "Robert Martin", "978-0132350884", 464));
            lib.addBook(new EBook("Effective Java", "Joshua Bloch", "978-0134685991", "PDF", 2.5));
        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    private static void addBookMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Add Book --");
            System.out.print("Title: ");
            String title = sc.nextLine().trim();
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            
            System.out.print("Author: ");
            String author = sc.nextLine().trim();
            if (author.isEmpty()) {
                throw new IllegalArgumentException("Author cannot be empty");
            }
            
            System.out.print("ISBN (format: XXX-XXXXXXXXXX): ");
            String isbn = sc.nextLine().trim();
            
            System.out.println("1. Paper Book\n2. E-Book");
            int type = sc.nextInt();
            sc.nextLine();
            
            if (type == 1) {
                System.out.print("Page Count: ");
                int pages = sc.nextInt();
                sc.nextLine();
                if (pages <= 0) {
                    throw new IllegalArgumentException("Page count must be positive");
                }
                lib.addBook(new PaperBook(title, author, isbn, pages));
            } else if (type == 2) {
                System.out.print("File Format: ");
                String format = sc.nextLine().trim();
                if (format.isEmpty()) {
                    throw new IllegalArgumentException("File format cannot be empty");
                }
                
                System.out.print("File Size (MB): ");
                double size = sc.nextDouble();
                sc.nextLine();
                if (size <= 0) {
                    throw new IllegalArgumentException("File size must be positive");
                }
                lib.addBook(new EBook(title, author, isbn, format, size));
            } else {
                throw new IllegalArgumentException("Invalid book type");
            }
            System.out.println("Book added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    private static void registerBorrowerMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Register Borrower --");
            System.out.print("Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            
            System.out.print("Student ID (8 alphanumeric characters): ");
            String id = sc.nextLine().trim();
            
            lib.registerBorrower(new Borrower(name, id));
            System.out.println("Borrower registered successfully!");
        } catch (Exception e) {
            System.out.println("Error registering borrower: " + e.getMessage());
        }
    }

    private static void borrowBookMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Borrow Book --");
            System.out.print("ISBN (format: XXX-XXXXXXXXXX): ");
            String isbn = sc.nextLine().trim();
            
            System.out.print("Student ID (8 alphanumeric characters): ");
            String id = sc.nextLine().trim();
            
            if (lib.borrowBook(isbn, id)) {
                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Borrow failed! Book might be unavailable or borrower not found.");
            }
        } catch (Exception e) {
            System.out.println("Error borrowing book: " + e.getMessage());
        }
    }

    private static void returnBookMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Return Book --");
            System.out.print("ISBN (format: XXX-XXXXXXXXXX): ");
            String isbn = sc.nextLine().trim();
            
            System.out.print("Student ID (8 alphanumeric characters): ");
            String id = sc.nextLine().trim();
            
            if (lib.returnBook(isbn, id)) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Return failed! No matching borrowing record found.");
            }
        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    private static void searchBooksMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Search Books --");
            System.out.print("Search query: ");
            String query = sc.nextLine().trim();
            
            List<Book> results = lib.searchBooks(query);
            
            if (results.isEmpty()) {
                System.out.println("No books found matching your search.");
            } else {
                System.out.println("\nSearch Results:");
                results.forEach(book -> System.out.println("- " + book));
            }
        } catch (Exception e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }

    private static void viewOverdueBooksMenu(LibraryService lib) {
        try {
            List<BorrowingProcess> overdueBooks = lib.getOverdueBooks();
            
            if (overdueBooks.isEmpty()) {
                System.out.println("\nNo overdue books found.");
            } else {
                System.out.println("\nOverdue Books:");
                overdueBooks.forEach(book -> System.out.println("- " + book));
            }
        } catch (Exception e) {
            System.out.println("Error viewing overdue books: " + e.getMessage());
        }
    }

    private static void viewBorrowerHistoryMenu(LibraryService lib, Scanner sc) {
        try {
            System.out.println("\n-- Borrower History --");
            System.out.print("Student ID (8 alphanumeric characters): ");
            String id = sc.nextLine().trim();
            
            List<BorrowingProcess> history = lib.getBorrowerTransactions(id);
            List<BorrowingProcess> activeBorrowings = lib.getActiveBorrowings(id);
            double totalFines = lib.getTotalFines(id);
            
            if (history.isEmpty()) {
                System.out.println("No borrowing history found for this student ID.");
            } else {
                System.out.println("\nBorrowing History:");
                history.forEach(transaction -> System.out.println("- " + transaction));
                
                if (!activeBorrowings.isEmpty()) {
                    System.out.println("\nCurrently Borrowed Books:");
                    activeBorrowings.forEach(book -> System.out.println("- " + book));
                }
                
                if (totalFines > 0) {
                    System.out.printf("\nTotal Fines: $%.2f\n", totalFines);
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing borrower history: " + e.getMessage());
        }
    }
}