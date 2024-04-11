package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserver extends UserGameCommand{
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    public JoinObserver(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        super.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
    public int getGameID() {
        return gameID;
    }
}
