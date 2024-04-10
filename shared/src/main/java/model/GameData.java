package model;

import chess.ChessGame;
import com.google.gson.Gson;

public class GameData {
    private Integer gameID;
    private String gameName;
    private String whiteUsername;
    private String blackUsername;
    private String chessGameString;

    public GameData(Integer gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.chessGameString = new Gson().toJson(new ChessGame());
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public String getGameName(){ return gameName; }
    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public void setChessGame(ChessGame chessGame) { this.chessGameString = new Gson().toJson(chessGame); }
    public ChessGame getChessGame(){ return new Gson().fromJson(chessGameString, ChessGame.class); }
    public void setWhiteUsername(String whiteUsername) { this.whiteUsername = whiteUsername; }
    public void setBlackUsername(String blackUsername) { this.blackUsername = blackUsername;}
    public Integer getGameID() {return this.gameID;}
}
