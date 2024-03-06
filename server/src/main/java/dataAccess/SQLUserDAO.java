package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUserDAO{

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
            try(var createTableStatement = conn.prepareStatement(createAuthTable)){
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }


    public UserData getUser(String username) throws DataAccessException, SQLException {
        try(var conn = getConnection()){
            String query = "SELECT username, userData FROM users WHERE username=?";
            try(var preparedStatement = conn.prepareStatement(query)) {
                if (username.matches("[a-zA-Z]+")) {
                    preparedStatement.setString(1, username);
                    var rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        String userDataString = rs.getString("userData");
                        return new Gson().fromJson(userDataString, UserData.class);
                    } else {
                        throw new DataAccessException("User doesn't exist");
                    }
                }else{
                    throw new SQLException("Username isn't acceptable");
                }
            }
        }
    }


    public String getPassword(String username) throws DataAccessException, SQLException {
        UserData userData = getUser(username);
        return userData.getPassword();
    }


    public void createUser(String username, String password, String email) throws SQLException, DataAccessException {
        try (var conn = getConnection()) {
            if (username.matches("[a-zA-Z]+") &&
                    password.matches("[a-zA-Z]+")){
                String userDataString = new Gson().toJson(new UserData(username, password, email));
                var addUser = "INSERT INTO users " +
                        "(username, userData) VALUES ('" +
                        username + "','" +
                        userDataString +
                        "');";
                try (var addUserStatement = conn.prepareStatement(addUser)) {
                    addUserStatement.executeUpdate();
                }
            }else{
                throw new SQLException("username, password, or email is incorrect format");
            }
        }
    }


    public void deleteAll() throws SQLException, DataAccessException {
        try(var conn = getConnection()){
            var deleteAll = "TRUNCATE TABLE users;";
            try(var addDeleteStatement = conn.prepareStatement(deleteAll)){
                addDeleteStatement.executeUpdate();
                instance = null;
            }
        }
    }

    public static SQLUserDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            instance = new SQLUserDAO();
            return instance;
        }
        return instance;
    }
}
