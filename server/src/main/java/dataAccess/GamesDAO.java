package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GamesDAO implements GameDAO{
    private Map<Integer, GameData> idToGameNames = new HashMap<>();
    private static GamesDAO instance;
    @Override
    public GameData getGame(int gameID) {
        return getInstance().idToGameNames.get(gameID);
    }

    @Override
    public int createGame(int gameID, String gameName) {
        getInstance().idToGameNames.put(gameID, new GameData(gameID, gameName));
        return gameID;
    }

    @Override
    public Vector<GameData> listGames() {
        return new Vector<>(getInstance().idToGameNames.values());
    }

    @Override
    public void updateGame(int gameID, String updatedGameString) {
        getInstance().idToGameNames.get(gameID).updateGameString(updatedGameString);
    }

    @Override
    public void deleteAll() {
        getInstance().idToGameNames.clear();
    }

    public synchronized static GamesDAO getInstance(){
        if(instance == null){
            instance = new GamesDAO();
        }
        return instance;
    }
}
