package dataAccess;

public interface AuthDAO {
    String getUsername(String authToken);
    void createAuth(String username, String authToken);
}
