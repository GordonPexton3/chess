package dataAccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GamesDAO implements GameDAO{
    private Map<String, GameData> idToGameNames = new HashMap<>();
    private static GamesDAO instance;
    @Override
    public GameData getGame(String gameID) {
        return instance.idToGameNames.get(gameID);
    }

    @Override
    public String createGame(String gameID, String gameName) {
        idToGameNames.put(gameID, new GameData(gameID, gameName));
        return gameID;
    }

    @Override
    public Collection<GameData> listGames() {
        return idToGameNames.values();
    }

    @Override
    public void updateGame(String gameID, String updatedGameString) {
        idToGameNames.get(gameID).updateGameString(updatedGameString);
    }

    public synchronized static GamesDAO getInstance(){
        if(instance == null){
            instance = new GamesDAO();
        }
        return instance;
    }
}
