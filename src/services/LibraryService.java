package services;

import models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LibraryService {
    private List<Book> books = new ArrayList<>();
    private List<Borrower> borrowers = new ArrayList<>();
    private List<BorrowingProcess> transactions = new ArrayList<>();
    
    // Validation patterns
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{3}-\\d{10}$");
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^[A-Z0-9]{8}$");

    // Book Management
    public void addBook(Book book) throws IllegalArgumentException {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (!ISBN_PATTERN.matcher(book.getIsbn()).matches()) {
            throw new IllegalArgumentException("Invalid ISBN format. Expected format: XXX-XXXXXXXXXX");
        }
        if (books.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))) {
            throw new IllegalArgumentException("A book with this ISBN already exists");
        }
        books.add(book);
    }

    public List<Book> getAllBooks() { 
        return new ArrayList<>(books); 
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return books.stream()
            .filter(b -> b.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                       b.getAuthor().toLowerCase().contains(query.toLowerCase()) || 
                       b.getIsbn().contains(query))
            .toList();
    }

    // Borrower Management
    public void registerBorrower(Borrower borrower) throws IllegalArgumentException {
        if (borrower == null) {
            throw new IllegalArgumentException("Borrower cannot be null");
        }
        if (!STUDENT_ID_PATTERN.matcher(borrower.getStudentId()).matches()) {
            throw new IllegalArgumentException("Invalid student ID format. Expected format: 8 alphanumeric characters");
        }
        if (borrowers.stream().anyMatch(b -> b.getStudentId().equals(borrower.getStudentId()))) {
            throw new IllegalArgumentException("A borrower with this student ID already exists");
        }
        borrowers.add(borrower);
    }

    // Borrow/Return Operations
    public boolean borrowBook(String isbn, String studentId) throws IllegalArgumentException {
        if (!ISBN_PATTERN.matcher(isbn).matches()) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("Invalid student ID format");
        }

        Optional<Book> book = books.stream()
            .filter(b -> b.getIsbn().equals(isbn) && b.isAvailable())
            .findFirst();

        Optional<Borrower> borrower = borrowers.stream()
            .filter(b -> b.getStudentId().equals(studentId))
            .findFirst();

        if (book.isPresent() && borrower.isPresent()) {
            borrower.get().borrowBook(book.get());
            transactions.add(new BorrowingProcess(book.get(), borrower.get(), LocalDate.now()));
            return true;
        }
        return false;
    }

    public boolean returnBook(String isbn, String studentId) throws IllegalArgumentException {
        if (!ISBN_PATTERN.matcher(isbn).matches()) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("Invalid student ID format");
        }

        Optional<BorrowingProcess> transaction = transactions.stream()
            .filter(t -> t.getBook().getIsbn().equals(isbn) &&
                       t.getBorrower().getStudentId().equals(studentId) &&
                       t.getReturnDate() == null)
            .findFirst();

        if (transaction.isPresent()) {
            transaction.get().setReturnDate(LocalDate.now());
            transaction.get().getBorrower().returnBook(transaction.get().getBook());
            return true;
        }
        return false;
    }

    // Overdue and Fine Management
    public List<BorrowingProcess> getOverdueBooks() {
        return transactions.stream()
            .filter(BorrowingProcess::isOverdue)
            .collect(Collectors.toList());
    }

    public List<BorrowingProcess> getBorrowerTransactions(String studentId) {
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("Invalid student ID format");
        }
        return transactions.stream()
            .filter(t -> t.getBorrower().getStudentId().equals(studentId))
            .collect(Collectors.toList());
    }

    public double getTotalFines(String studentId) {
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("Invalid student ID format");
        }
        return transactions.stream()
            .filter(t -> t.getBorrower().getStudentId().equals(studentId))
            .mapToDouble(BorrowingProcess::getFine)
            .sum();
    }

    public List<BorrowingProcess> getActiveBorrowings(String studentId) {
        if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
            throw new IllegalArgumentException("Invalid student ID format");
        }
        return transactions.stream()
            .filter(t -> t.getBorrower().getStudentId().equals(studentId) && 
                        t.getReturnDate() == null)
            .collect(Collectors.toList());
    }
}