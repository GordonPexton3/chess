package server;

import model.GameData;

import java.util.Vector;

public class MyResponse {

    private String authToken;
    private String username;
    private Vector<GameData> games;
    private Integer gameID;
    private String message;
    private Integer status;

    public String getUsername() {
        return username;
    }

    public Integer getStatus() { return status; }

    public String getAuthToken(){
        return authToken;
    }

    public Vector<GameData> getGames(){ return games; }

    public Integer getGameID(){
        return gameID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGames(Vector<GameData> games) {
        this.games = games;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Integer status){ this.status = status; }
}
