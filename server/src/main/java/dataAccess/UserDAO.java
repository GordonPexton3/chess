package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    String getPassword(String username);
    void createUser(String username, String password, String email);
    void deleteAll();
}
