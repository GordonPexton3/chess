package server;

import chess.ChessGame;
import java.util.Vector;

public class myResponse {

    private String authToken;
    private String username;
    private Vector<ChessGame> games;
    private String gameID;
    private String message;
    private int status;

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public Vector<ChessGame> getGames() {
        return games;
    }

    public String getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() { return status; }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGames(Vector<ChessGame> games) {
        this.games = games;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status){ this.status = status; }
}
