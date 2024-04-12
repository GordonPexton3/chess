package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{
    int gameID;
    public Leave(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        super.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }
}