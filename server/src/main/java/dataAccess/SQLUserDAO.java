package dataAccess;

import com.google.gson.Gson;
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
        try (var conn = getConnection()) {
            String userDataString = new Gson().toJson(new UserData(username, password,email));
            var addUser = "INSERT INTO user " +
                    "(username, userData) VALUES (" +
                    username + "," +
                    userDataString +
                    ")";
            try (var addUserStatement = conn.prepareStatement(addUser)) {
                addUserStatement.executeUpdate();
            }catch(SQLException e){
                System.out.println("Your SQL code stinks \n" + e);
            }
        }catch(SQLException | DataAccessException e){
            System.out.println("Couldn't tell ya!" + e);
        }
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
