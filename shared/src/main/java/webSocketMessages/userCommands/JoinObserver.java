package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    private int gameID;
    private String userID;
    public JoinObserver(String authToken, int gameID, String userID) {
        super(authToken);
        this.gameID = gameID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public int getGameID() {
        return gameID;
    }
}
