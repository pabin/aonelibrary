package business;

import java.io.Serializable;

public class CheckoutEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String issuedDate;
    private int issuedDuration; // Added issuedDuration
    private BookCopy bookCopy;


    public CheckoutEntry(String id, String issuedDate, int issuedDuration, BookCopy bookCopy) {
        this.issuedDate = issuedDate;
        this.issuedDuration = issuedDuration;
        this.bookCopy = bookCopy;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getIssuedDate() { return issuedDate; }
    public void setIssuedDate(String issuedDate) { this.issuedDate = issuedDate; }
    public int getIssuedDuration() { return issuedDuration; } // Added getter for issuedDuration
    public void setIssuedDuration(int issuedDuration) { this.issuedDuration = issuedDuration; } // Added setter for issuedDuration
    public BookCopy getBookCopy() { return bookCopy; }
    public void setBookCopy(BookCopy bookCopy) { this.bookCopy = bookCopy; }
    public String getId() {
        return id;
    }
    @Override
    public String toString() {
        return "Id: " + id + " issued date: " + issuedDate + ", issued duration: " + issuedDuration + " days";
    }
}
