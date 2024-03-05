package dataAccess;

import model.GameData;

import java.util.Vector;

public interface GameDAO {
    GameData getGame (Integer gameID) throws DataAccessException;
    Integer createGame(Integer gameID, String gameName);
    Vector<GameData> listGames();
    void updateGame(Integer gameID, GameData gameDataObject);
    void deleteAll();

}
