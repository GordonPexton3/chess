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
            try(var createTableStatement = conn.prepareStatement(createAuthTable)){
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
    public UserData getUser(String username){
        try(var conn = getConnection()){
            String query = "SELECT username, userData FROM users WHERE username=?";
            try(var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    String userDataString = rs.getString("userData");
                    return new Gson().fromJson(userDataString, UserData.class);
                }else{
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problems in Get User\n" + e);
        }
    }

        @Override
    public String getPassword(String username) {
        UserData userData = getUser(username);
        return userData.getPassword();
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
            try(var addUserStatement = conn.prepareStatement(addUser)){
                addUserStatement.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println("Problems in create user\n" + e);
        }
    }

    @Override
    public void deleteAll() {
        try(var conn = getConnection()){
            var deleteAll = "DROP TABLE users;";
            try(var addDeleteStatement = conn.prepareStatement(deleteAll)){
                addDeleteStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new RuntimeException("Problem in delete all\n" + e);
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
