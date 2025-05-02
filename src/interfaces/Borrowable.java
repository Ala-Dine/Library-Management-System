package interfaces;

public interface Borrowable {
    boolean isAvailable();
    void setAvailable(boolean available);
    String getBorrowInfo();
}