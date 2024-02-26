package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData getGame(int gameID);
    int createGame(int gameID, String gameName);
    Collection<GameData> listGames();
    void updateGame(int gameID, String updatedGameString);
    void deleteAll();

}
