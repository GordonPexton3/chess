package dataAccess;

public interface AuthDAO {
    String getUsername(String authToken) throws DataAccessException;
    String createAuth(String username, String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void deleteAll();
}
