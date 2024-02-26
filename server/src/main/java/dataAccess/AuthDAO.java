package dataAccess;

public interface AuthDAO {
    String getUsername(String authToken);
    String createAuth(String username, String authToken);
    void deleteAuth(String authToken);
    void deleteAll();
}
