package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{
    int gameID;
    String username;
    public Resign(String authToken, int gameID, String username) {
        super(authToken);
        this.gameID = gameID;
        this.username = username;
    }
    public String getUsername(){ return username; }

    public int getGameID() {
        return gameID;
    }
}
