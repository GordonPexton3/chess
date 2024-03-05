package dataAccess;

import com.google.gson.Gson;
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
                gameID INT NOT NULL,
                gameData VARCHAR(255) NOT NULL, 
                INDEX (gameID)
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
            throw new RuntimeException("Error in get Connection\n" + e);
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try(var conn = getConnection()){
            String query = "SELECT gameID, gameData FROM games WHERE gameID=?";
            try(var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    String gameDataString = rs.getString("gameData");
                    GameData gameDataObject = new Gson().fromJson(gameDataString, GameData.class);
                    return gameDataObject;
                }else{
                    throw new DataAccessException("Game doesn't exist");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problems in SQL getGame\n" + e);
        }
    }

    @Override
    public Integer createGame(Integer gameID, String gameName) {
        try (var conn = getConnection()) {
            String gameData = new Gson().toJson(new GameData(gameID, gameName));
            var addGame = "INSERT INTO games " +
                    "(gameID, gameData) VALUES ('" +
                    gameID + "','" +
                    gameData +
                    "');";
            try(var addUserStatement = conn.prepareStatement(addGame)){
                addUserStatement.executeUpdate();
                return gameID;
            }
        }catch(SQLException e){
            System.out.println("Problems in createGame\n" + e);
        }
        throw new RuntimeException("Problems in createGame\n");
    }

    @Override
    public Vector<GameData> listGames() {
        Vector<GameData> gamesList = new Vector<>();
        try(var conn = getConnection()){
            String query = "SELECT gameID, gameData FROM games";
            try(var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeQuery();
                while(rs.next()){
                    String gameDataString = rs.getString("gameData");
                    gamesList.add(new Gson().fromJson(gameDataString, GameData.class));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problems in SQL getGame\n" + e);
        }
        return gamesList;
    }

    @Override
    public void updateGame(Integer gameID, GameData gameDataObject){
        try(var conn = getConnection()){
            String gameDataString = new Gson().toJson(gameDataObject);
            String updateString = "UPDATE games SET gameData=? WHERE gameID=?";
            try (var updateGameStatement = conn.prepareStatement(updateString)) {
                updateGameStatement.setString(1, gameDataString);
                updateGameStatement.setInt(2, gameID);

                updateGameStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Problems in SQL getGame\n" + e);
        }
    }

    @Override
    public void deleteAll() {
        try(var conn = getConnection()){
            var deleteAll = "DROP TABLE games;";
            try (var addDeleteStatement = conn.prepareStatement(deleteAll)) {
                addDeleteStatement.executeUpdate();
            }
        }catch(SQLException e){
            throw new RuntimeException("Problem in delete all\n" + e);
        }
    }

    public static SQLGameDAO getInstance() throws SQLException, DataAccessException {
        if(instance == null){
            return new SQLGameDAO();
        }
        return instance;
    }
}
