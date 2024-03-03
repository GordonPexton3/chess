package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    private static SQLUserDAO instance;

    public SQLUserDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {

            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                userData VARCHAR(255) NOT NULL,
                INDEX (username)
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws DataAccessException{
        return DatabaseManager.getConnection();
    }

    @Override
    public UserData getUser(String username) {
        // TODO create the user Get User Function
        return null;
    }

    @Override
    public String getPassword(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        INSERT INTO pet (name, type) VALUES ('Puddles', 'cat');
    }

    @Override
    public void deleteAll() {

    }

    public static SQLUserDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            return new SQLUserDAO();
        }
        return instance;
    }
}
