package model;

public class GameData {
    private Integer gameID;
    private String gameName;
    private String whiteUsername;
    private String blackUsername;
    private String gameString;

    public GameData(Integer gameID, String gameName) {
        this.gameID = gameID;
        this.gameName = gameName;
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

    public void updateGameString(String gameString) { this.gameString = gameString; }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public Integer getGameID() {
        return this.gameID;
    }
}
