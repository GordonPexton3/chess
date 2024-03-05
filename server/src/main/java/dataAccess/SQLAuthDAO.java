package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO{


    private static SQLAuthDAO instance;

    public SQLAuthDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {
            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS auth (
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                INDEX (authToken)
            )""";
            try(var createTableStatement = conn.prepareStatement(createAuthTable)){
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }


    public String getUsername(String authToken) throws DataAccessException, SQLException {
        try(var conn = getConnection()){
            String query = "SELECT authToken, username FROM auth WHERE authToken=?";
            try(var preparedStatement = conn.prepareStatement(query)){
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    return rs.getString("username");
                }
            }
        }
    }


    public void createAuth(String username, String authToken) throws SQLException{
        try(var conn = getConnection()) {
            var addUser = "INSERT INTO auth " +
                    "(authToken, username) VALUES ('" +
                    username + "','" +
                    authToken +
                    "');";
            try (var addAuthStatement = conn.prepareStatement(addUser)) {
                addAuthStatement.executeUpdate();
            }
        }
    }


    public void deleteAuth(String authToken) throws SQLException {
        try(var conn = getConnection()){
            var deleteAuth = "DELETE FROM auth WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(deleteAuth)){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
    }


    public void deleteAll() throws SQLException {
        try(var conn = getConnection()){
            var deleteAll = "DROP TABLE auth;";
            try (var addDeleteStatement = conn.prepareStatement(deleteAll)){
                addDeleteStatement.executeUpdate();
            }
        }
    }

    public static SQLAuthDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            instance = new SQLAuthDAO();
            return instance;
        }
        return instance;
    }
}
