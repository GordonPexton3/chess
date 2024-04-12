package dataAccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class SQLGameDAO{

    private static SQLGameDAO instance;

    public SQLGameDAO() throws SQLException, DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {
            var createAuthTable = """
            CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL,
                gameData TEXT NOT NULL,
                INDEX (gameID)
            )""";
            try(var createTableStatement = conn.prepareStatement(createAuthTable)){
                createTableStatement.executeUpdate();
            }
        }
    }

    private Connection getConnection() throws DataAccessException{
        return DatabaseManager.getConnection();
    }


    public GameData getGame(Integer gameID) throws DataAccessException, SQLException {
        try(var conn = getConnection()){
            String query = "SELECT gameID, gameData FROM games WHERE gameID=?";
            try(var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    String gameDataString = rs.getString("gameData");
                    return new Gson().fromJson(gameDataString, GameData.class);
                }else{
                    throw new DataAccessException("Game doesn't exist");
                }
            }
        }
    }


    public void createGame(Integer gameID, String gameName) throws SQLException, DataAccessException {
        try (var conn = getConnection()) {
            String gameData = new Gson().toJson(new GameData(gameID, gameName));
//            GameData gameData = new GameData(gameID, gameName);
            var addGame = "INSERT INTO games " +
                    "(gameID, gameData) VALUES ('" +
                    gameID + "','" +
                    gameData +
                    "');";
            try(var addUserStatement = conn.prepareStatement(addGame)){
                addUserStatement.executeUpdate();
            }
        }
    }


    public Vector<GameData> listGames() throws SQLException, DataAccessException {
        Vector<GameData> gamesList = new Vector<>();
        try (var conn = getConnection()) {
            String query = "SELECT gameID, gameData FROM games";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String gameDataString = rs.getString("gameData");
                    gamesList.add(new Gson().fromJson(gameDataString, GameData.class));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gamesList;
    }


    public void updateGame(Integer gameID, GameData gameDataObject) throws SQLException, DataAccessException{
        try(var conn = getConnection()){
            String gameDataString = new Gson().toJson(gameDataObject);
            String updateString = "UPDATE games SET gameData=? WHERE gameID=?";
            try (var updateGameStatement = conn.prepareStatement(updateString)) {
                updateGameStatement.setString(1, gameDataString);
                updateGameStatement.setInt(2, gameID);

                updateGameStatement.executeUpdate();
            }
        }
    }


    public void deleteAll() throws SQLException, DataAccessException{
        try(var conn = getConnection()) {
            var deleteAll = "TRUNCATE TABLE games;";
            try (var addDeleteStatement = conn.prepareStatement(deleteAll)) {
                addDeleteStatement.executeUpdate();
                instance = null;
            }
        }
    }

    public static SQLGameDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            instance = new SQLGameDAO();
            return instance;
        }
        return instance;
    }
}
