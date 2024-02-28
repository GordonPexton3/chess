package dataAccess;

public interface AuthDAO {
    String getUsername(String authToken) throws DataAccessException;
    String createAuth(String username, String authToken);
    void deleteAuth(String authToken);
    void deleteAll();
}
