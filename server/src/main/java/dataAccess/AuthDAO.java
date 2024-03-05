package dataAccess;

import java.sql.SQLException;

public interface AuthDAO {
    String getUsername(String authToken) throws DataAccessException;
    void createAuth(String username, String authToken) throws SQLException;
    void deleteAuth(String authToken);
    void deleteAll();
}
