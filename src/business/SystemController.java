package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
    public static Auth currentAuth = null;

    public void login(String id, String password) throws LoginException {
        DataAccess da = new DataAccessFacade();
        HashMap<String, User> map = da.readUserMap();
        if (!map.containsKey(id)) {
            throw new LoginException("ID " + id + " not found");
        }
        String passwordFound = map.get(id).getPassword();
        if (!passwordFound.equals(password)) {
            throw new LoginException("Password incorrect");
        }
        currentAuth = map.get(id).getAuthorization();
    }

    @Override
    public List<String> allMemberIds() {
        DataAccess da = new DataAccessFacade();
        List<String> retval = new ArrayList<>();
        retval.addAll(da.readMemberMap().keySet());
        return retval;
    }

    @Override
    public List<String> allBookIds() {
        DataAccess da = new DataAccessFacade();
        List<String> retval = new ArrayList<>();
        retval.addAll(da.readBooksMap().keySet());
        return retval;
    }


    @Override
    public void addMember(LibraryMember member) {
        DataAccess da = new DataAccessFacade();
        da.saveNewMember(member);
    }


    public boolean checkIfMemberExists(String phone) {
        DataAccess da = new DataAccessFacade();
        List<LibraryMember> members = da.readMemberMap().values().stream().toList();
        return members.stream().anyMatch(m -> m.getTelephone().equals(phone));
    }

    @Override
    public Optional<LibraryMember> getMember(String id) {
        DataAccess da = new DataAccessFacade();
        return da.readMemberMap().values().stream().filter(x -> x.getMemberId().equals(id)).findFirst();
    }

    @Override
    public List<CheckoutEntry> getMemberCheckoutRecordForMember(String memberID) {
        DataAccess da = new DataAccessFacade();
        return da.readCheckoutEntryMap().values().stream().filter(x ->
                x.getMember().getMemberId().equals(memberID)
        ).toList();
    }

    @Override
    public List<CheckoutEntry> getMemberCheckoutRecordForBook(String ISBN) {
        DataAccess da = new DataAccessFacade();
        return da.readCheckoutEntryMap().values().stream().filter(x ->
                x.getBookCopy().getBook().getIsbn().equals(ISBN)
        ).toList();
    }

    @Override
    public List<Book> getBooks() {
        DataAccess da = new DataAccessFacade();
        return da.readBooksMap().values().stream().toList();
    }

    @Override
    public List<LibraryMember> getMembers() {
        DataAccess da = new DataAccessFacade();
        return da.readMemberMap().values().stream().toList();
    }

    @Override
    public List<CheckoutEntry> getCheckOutEntries() {
        DataAccess da = new DataAccessFacade();
        return da.readCheckoutEntryMap().values().stream().toList();
    }

    @Override
    public void logout() {
        SystemController.currentAuth = null;
    }
}
