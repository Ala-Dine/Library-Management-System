package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowingProcess {
    private static final int BORROWING_PERIOD_DAYS = 14;
    
    private Book book;
    private Borrower borrower;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public BorrowingProcess(Book book, Borrower borrower, LocalDate borrowDate) {
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(BORROWING_PERIOD_DAYS);
        this.fine = 0.0;
    }

    // Getters
    public Book getBook() { return book; }
    public Borrower getBorrower() { return borrower; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getFine() { return fine; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            this.fine = daysLate * 0.50; // $0.50 per day late
        }
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (returnDate != null) {
            return ChronoUnit.DAYS.between(dueDate, returnDate);
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s borrowed by %s on %s", 
            book.getTitle(), borrower.getName(), borrowDate));
        
        if (returnDate != null) {
            sb.append(String.format(" | Returned: %s", returnDate));
            if (fine > 0) {
                sb.append(String.format(" | Fine: $%.2f", fine));
            }
        } else if (isOverdue()) {
            sb.append(String.format(" | Overdue by %d days | Fine: $%.2f", 
                getDaysOverdue(), fine));
        } else {
            sb.append(String.format(" | Due: %s", dueDate));
        }
        
        return sb.toString();
    }
}