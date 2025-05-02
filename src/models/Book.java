package models;

import interfaces.Borrowable;

public abstract class Book implements Borrowable {
    private String title;
    private String author;
    private String isbn;
    private boolean available = true;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }

    // Borrowable Interface Implementation
    @Override
    public boolean isAvailable() { return available; }

    @Override
    public void setAvailable(boolean available) { 
        this.available = available; 
    }

    @Override
    public String getBorrowInfo() {
        return String.format("[%s] %s by %s - %s", 
            isbn, title, author, available ? "Available" : "Borrowed");
    }

    // Abstract method for polymorphism
    public abstract String getBookType();

    @Override
    public String toString() {
        return getBorrowInfo();
    }
}