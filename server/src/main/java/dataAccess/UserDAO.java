package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData getUser(String username);
    String getPassword(String username);
    void createUser(String username, String password, String email);
    void deleteAll();
}
