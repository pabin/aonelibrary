package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
    public void login(String id, String password) throws LoginException;

    public List<String> allMemberIds();

    public List<String> allBookIds();

    void addMember(LibraryMember member);

    void updateMember(LibraryMember libraryMember);

    public boolean checkIfMemberExists(String phone);

    public void logout();
}
