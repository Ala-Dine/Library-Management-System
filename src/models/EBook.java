package models;

public class EBook extends Book {
    private String fileFormat;
    private double fileSizeMB;

    public EBook(String title, String author, String isbn, 
                String fileFormat, double fileSizeMB) {
        super(title, author, isbn);
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
    }

    @Override
    public String getBookType() {
        return String.format("E-Book (%s, %.2f MB)", fileFormat, fileSizeMB);
    }

    public String getFileFormat() { return fileFormat; }
    public double getFileSizeMB() { return fileSizeMB; }
}