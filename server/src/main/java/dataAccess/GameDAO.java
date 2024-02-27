package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData getGame(Integer gameID);
    Integer createGame(Integer gameID, String gameName);
    Collection<GameData> listGames();
    void deleteAll();

}
