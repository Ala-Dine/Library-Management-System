package models;

import java.util.ArrayList;
import java.util.List;

public class Borrower {
    private String name;
    private String studentId;
    private List<Book> borrowedBooks = new ArrayList<>();

    public Borrower(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    // Getters
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public List<Book> getBorrowedBooks() { return new ArrayList<>(borrowedBooks); }

    // Borrow/return operations
    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            borrowedBooks.add(book);
            book.setAvailable(false);
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.setAvailable(true);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) - Books Borrowed: %d", 
            name, studentId, borrowedBooks.size());
    }
}