package dataaccess;

import java.util.HashMap;

import business.Book;
import business.CheckoutEntry;
import business.LibraryMember;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public HashMap<String, CheckoutEntry> readCheckoutEntryMap();
	public void saveNewMember(LibraryMember member); 
}
