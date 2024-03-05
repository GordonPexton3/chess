package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{


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
        try(var conn = getConnection()){
            String query = "SELECT authToken, username FROM auth WHERE authToken=?";
            try(var preparedStatement = conn.prepareStatement(query)){
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if(rs.next()) {
                    return rs.getString("username");
                }
            }
        }catch(SQLException e) {
            System.out.println("problem in getUsername\n" + e);
            throw new RuntimeException(e);
        }
        throw new DataAccessException("Username does not exist");
    }

    @Override
    public void createAuth(String username, String authToken){
        try(var conn = getConnection()){
            var addUser = "INSERT INTO auth " +
                    "(authToken, username) VALUES ('" +
                    username + "','" +
                    authToken +
                    "');";
            try(var addAuthStatement = conn.prepareStatement(addUser)){
                addAuthStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new RuntimeException("In create Auth\n" + e);
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        try(var conn = getConnection()){
            var deleteAuth = "DELETE FROM auth WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(deleteAuth)){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new RuntimeException("Problem in deleteAuth" + e);
        }
    }

    @Override
    public void deleteAll() {
        try(var conn = getConnection()){
            var deleteAll = "DROP TABLE auth;";
            try (var addDeleteStatement = conn.prepareStatement(deleteAll)){
                addDeleteStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new RuntimeException("Problem in delete all" + e);
        }
    }

    public static SQLAuthDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            return new SQLAuthDAO();
        }
        return instance;
    }
}
