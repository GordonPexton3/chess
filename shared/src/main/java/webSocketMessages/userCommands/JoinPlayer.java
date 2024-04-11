package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor playerColor;
    private String username;

    public JoinPlayer(String authToken, int gameID, String username, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.username = username;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public String getUsername() {
        return username;
    }
}
