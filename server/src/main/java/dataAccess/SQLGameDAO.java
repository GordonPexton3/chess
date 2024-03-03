package dataAccess;

import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class SQLGameDAO implements GameDAO{

    private static SQLGameDAO instance;

    public SQLGameDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {

            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS games (
                gameID VARCHAR(255) NOT NULL,
                gameData VARCHAR(255) NOT NULL, 
                INDEX (gameID)
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }catch(SQLException e){
                System.out.println("Problem with configuring game table");
            }
        }
    }

    private Connection getConnection() throws DataAccessException{
        return DatabaseManager.getConnection();
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Integer createGame(Integer gameID, String gameName) {
        return null;
    }

    @Override
    public Vector<GameData> listGames() {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    public static SQLGameDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            return new SQLGameDAO();
        }
        return instance;
    }
}
