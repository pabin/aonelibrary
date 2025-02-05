package business;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CheckoutEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String issuedDate;
    private int issuedDuration;
    private String dueDate;
    private BookCopy bookCopy;
    private LibraryMember member;


    public CheckoutEntry(String id, String issuedDate, int issuedDuration, BookCopy bookCopy, LibraryMember member) {
        this.issuedDate = issuedDate;
        this.issuedDuration = issuedDuration;
        this.bookCopy = bookCopy;
        this.id = id;
        this.member = member;


    }


    public String getDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateObj = LocalDate.parse(issuedDate, formatter);
        LocalDate newDateObj = dateObj.plusDays(issuedDuration);
        return newDateObj.format(formatter);
    }

    public LibraryMember getMember(){
        return member;
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
