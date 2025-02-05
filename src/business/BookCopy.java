package business;

import librarysystem.CheckoutWindow;

import java.io.Serializable;
import java.util.List;

/**
 * Immutable class
 */
final public class BookCopy implements Serializable {
	
	private static final long serialVersionUID = -63976228084869815L;
	private Book book;
	private int copyNum;
	private boolean isAvailable;
	private List<CheckoutWindow> checkoutEntries;
	BookCopy(Book book, int copyNum, boolean isAvailable) {
		this.book = book;
		this.copyNum = copyNum;
		this.isAvailable = isAvailable;
	}
	
	BookCopy(Book book, int copyNum) {
		this.book = book;
		this.copyNum = copyNum;
	}
	
	
	public boolean isAvailable() {
		return isAvailable;
	}

	public int getCopyNum() {
		return copyNum;
	}
	
	public Book getBook() {
		return book;
	}

	public List<CheckoutWindow> getCheckoutEntries() {
		return checkoutEntries;
	}

	public void setCheckoutEntries(List<CheckoutWindow> checkoutEntries) {
		this.checkoutEntries = checkoutEntries;
	}

	public void changeAvailability() {
		isAvailable = !isAvailable;
	}
	
	@Override
	public boolean equals(Object ob) {
		if(ob == null) return false;
		if(!(ob instanceof BookCopy)) return false;
		BookCopy copy = (BookCopy)ob;
		return copy.book.getIsbn().equals(book.getIsbn()) && copy.copyNum == copyNum;
	}

	@Override
	public String toString() {
		return "copy no: " + this.copyNum + ", available: " + this.isAvailable;
	}
	
}
