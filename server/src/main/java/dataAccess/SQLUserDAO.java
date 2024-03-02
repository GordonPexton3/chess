package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie");
    }

    void makeSQLCalls() throws SQLException {
        try (var conn = getConnection()) {
            // Execute SQL statements on the connection here
        }
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public String getPassword(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {

    }

    @Override
    public void deleteAll() {

    }
}
