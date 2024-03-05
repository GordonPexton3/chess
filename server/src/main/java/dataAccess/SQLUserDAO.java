package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLOutput;

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

    private Connection getConnection(){
        try{
            return DatabaseManager.getConnection();
        }catch (DataAccessException e){
            System.out.println("Error in get Connection\n" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var conn = getConnection();
        try (var preparedStatement = conn.prepareStatement("SELECT id, name, type FROM pet WHERE type=?")) {
            preparedStatement.setString(1, findType);
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var name = rs.getString("name");
                    var type = rs.getString("type");

                    System.out.printf("id: %d, name: %s, type: %s%n", id, name, type);
                }
            }
        }catch(SQLException e){
            System.out.println("Your request stinks in getUser SQLUserDAO\n" + e);
            throw new RuntimeException(e);
        }
    }

        @Override
    public String getPassword(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        try (var conn = getConnection()) {
            String userDataString = new Gson().toJson(new UserData(username, password,email));
            var addUser = "INSERT INTO users " +
                    "(username, userData) VALUES ('" +
                    username + "','" +
                    userDataString +
                    "');";
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
