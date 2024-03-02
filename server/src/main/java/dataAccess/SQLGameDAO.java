package dataAccess;

import model.GameData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDAO{

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie");
    }

    void makeSQLCalls() throws SQLException {
        try (var conn = getConnection()) {
            // Execute SQL statements on the connection here
        }
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
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
