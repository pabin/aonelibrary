package business;

public class CheckoutEntry {
    private String id;
    private String issuedDate;
    private String dueDate;
    private BookCopy bookCopy;
    private boolean isInitialized = false;

    public CheckoutEntry() {}
    public CheckoutEntry(String issuedDate, String dueDate, BookCopy bookCopy) {
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
        this.bookCopy = bookCopy;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getIssuedDate() { return issuedDate; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public BookCopy getBookCopy() { return bookCopy; }
    public void setBookCopy(BookCopy bookCopy) { this.bookCopy = bookCopy; }

    @Override
    public String toString() {
        return "Id: " + id + " issued date: " + issuedDate + ", due date: " + dueDate;
    }

}
