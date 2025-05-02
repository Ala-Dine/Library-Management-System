package models;

public class PaperBook extends Book {
    private int pageCount;

    public PaperBook(String title, String author, String isbn, int pageCount) {
        super(title, author, isbn);
        this.pageCount = pageCount;
    }

    @Override
    public String getBookType() {
        return String.format("Paper Book (%d pages)", pageCount);
    }

    public int getPageCount() {
        return pageCount;
    }
}