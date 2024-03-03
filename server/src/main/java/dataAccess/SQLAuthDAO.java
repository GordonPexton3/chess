package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{


    private static SQLAuthDAO instance;

    public SQLAuthDAO() {
        try{
            configureDatabase();
        }catch(SQLException e){
            System.out.println(e);
        }catch(DataAccessException e){
            System.out.println("WHAT DO I DO WITH THIS?" + e);
        }
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
                auth_token VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL, 
                INDEX (auth_token)
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws DataAccessException{
        return DatabaseManager.getConnection();
    }

    void makeSQLCalls() throws DataAccessException, SQLException {
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
        //"DROP TABLE pet;"
    }

    public static SQLAuthDAO getInstance(){
        if(instance == null){
            return new SQLAuthDAO();
        }
        return instance;
    }
}
