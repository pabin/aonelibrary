package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



final public class LibraryMember extends Person implements Serializable {
	private String memberId;
	List<CheckoutEntry> checkoutEntries;

	public LibraryMember() {
		super();
		checkoutEntries = new ArrayList<>();
	}


	public List<CheckoutEntry> getCheckoutEntries() {
		return checkoutEntries;
	}

	public void setCheckoutEntries(List<CheckoutEntry> checkoutEntries) {
		this.checkoutEntries = checkoutEntries;
	}

	public LibraryMember(String memberId, String fname, String lname, String tel, Address add) {
		super(fname,lname, tel, add);
		this.memberId = memberId;		
	}
	
	public String getMemberId() {
		return memberId;
	}

	
	
	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress() + "Checkout Entries: " + getCheckoutEntries();
	}

	private static final long serialVersionUID = -2226197306790714013L;
}
