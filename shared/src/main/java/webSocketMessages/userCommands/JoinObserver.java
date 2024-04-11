package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserver extends UserGameCommand{
    private final int gameID;
    private final String username;
    private final ChessGame.TeamColor playerColor;
    public JoinObserver(String authToken, int gameID, String username, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.username = username;
        this.playerColor = playerColor;
    }
    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }
}
