package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{


    public static void SQLAuthDAO(){
        try() // TODO make a constructor to try connecting, create, configuring.
    }
    /*
    Get a connection to the RDBMS.
    Create the pet store database if it doesn't exist.
    Create the pet table if it doesn't exist.
     */
    void configureDatabase() throws SQLException {
        try (var conn = getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS auth_data");
            createDbStatement.executeUpdate();

            conn.setCatalog("auth_data");

            var createPetTable = """
            CREATE TABLE  IF NOT EXISTS auth (
                auth_token VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
            )""";

            try (var createTableStatement = conn.prepareStatement(createPetTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    Connection getConnection() throws SQLException {
        return DatabaseManager.getConnection();
    }

    void makeSQLCalls() throws SQLException {
        try (var conn = getConnection()) {
            // Execute SQL statements on the connection here
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String createAuth(String username, String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void deleteAll() {

    }
}
