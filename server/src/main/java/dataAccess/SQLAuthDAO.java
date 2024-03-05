package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{


    private static SQLAuthDAO instance;

    public SQLAuthDAO() throws SQLException, DataAccessException {
        configureDatabase();
        /*
        Get a connection to the RDBMS.
        Create the pet store database if it doesn't exist.
        Create the pet table if it doesn't exist.
         */
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

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }catch(SQLException e){
                System.out.println("Problem creating auth database");
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
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void createAuth(String username, String authToken) {
        try(var conn = getConnection()){
            var addUser = "INSERT INTO auth " +
                    "(authToken, username) VALUES ('" +
                    authToken + "','" +
                    username +
                    "');";
            try (var addAuthStatement = conn.prepareStatement(addUser)) {
                addAuthStatement.executeUpdate();
            }catch(SQLException e){
                System.out.println("Your SQL code stinks in createAuth\n" + e);
            }
        }catch (SQLException e){
            System.out.println("In create Auth" + e);
        }
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void deleteAll() {
        //"DROP TABLE pet;"
    }

    public static SQLAuthDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            return new SQLAuthDAO();
        }
        return instance;
    }
}
