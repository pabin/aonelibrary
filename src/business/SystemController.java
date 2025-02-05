package business;

import java.lang.reflect.Member;
import java.util.*;

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

    @Override
    public void updateMember(LibraryMember libraryMember) {
        DataAccess da = new DataAccessFacade();

        da.saveNewMember(libraryMember);
        System.out.println(da.readMemberMap());

    }

    public boolean checkIfMemberExists(String phone) {
        DataAccess da = new DataAccessFacade();
        List<LibraryMember> members = da.readMemberMap().values().stream().toList();
        return members.stream().anyMatch(m -> m.getTelephone().equals(phone));
    }

    @Override
    public void logout() {
        SystemController.currentAuth = null;
    }
}
