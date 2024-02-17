package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData getGame(String gameID);
    String createGame(String gameID, String gameName);
    Collection<GameData> listGames();
    void updateGame(String gameID, String updatedGameString);

}
