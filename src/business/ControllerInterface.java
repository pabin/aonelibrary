package business;

import java.util.List;
import java.util.Optional;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {

    public void login(String id, String password) throws LoginException;

    public List<String> allMemberIds();

    public List<String> allBookIds();

    void addMember(LibraryMember member);

    public boolean checkIfMemberExists(String phone);

    public Optional<LibraryMember> getMember(String id);

    public  List<LibraryMember> getMembers();

    public List<Book> getBooks();

    public void logout();

}
