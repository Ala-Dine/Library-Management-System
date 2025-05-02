package tests;

import models.*;
import services.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class LibraryServiceTest {
    private LibraryService library;
    private Book paperBook;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        library = new LibraryService();
        paperBook = new PaperBook("Clean Code", "Robert Martin", "978-0132350884", 464);
        borrower = new Borrower("John Doe", "STD123");
        library.addBook(paperBook);
        library.registerBorrower(borrower);
    }

    @Test
    void testSuccessfulBorrow() {
        assertTrue(library.borrowBook(paperBook.getIsbn(), borrower.getStudentId()));
        assertFalse(paperBook.isAvailable());
    }

    @Test
    void testReturnBook() {
        library.borrowBook(paperBook.getIsbn(), borrower.getStudentId());
        assertTrue(library.returnBook(paperBook.getIsbn(), borrower.getStudentId()));
        assertTrue(paperBook.isAvailable());
    }
}