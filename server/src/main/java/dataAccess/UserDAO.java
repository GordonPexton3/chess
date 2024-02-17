package dataAccess;

public interface UserDAO {
    String getPassword(String username);
    void createUser(String username, String password, String email);
}
