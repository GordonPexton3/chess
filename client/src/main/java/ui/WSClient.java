package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.MyError;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class WSClient extends Endpoint {
    public Session session;
    private ChessGame lastGameState;
    private final DrawBoard db = new DrawBoard();

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {

            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            switch (serverMessage.getServerMessageType()){
                case ServerMessage.ServerMessageType.ERROR -> Error((MyError) serverMessage);
                case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame((LoadGame) serverMessage);
                case ServerMessage.ServerMessageType.NOTIFICATION -> notification((Notification) serverMessage);
            }
        });
    }

    public void send(UserGameCommand command) {
        try{
            String strMsg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(strMsg);
        }catch(Exception e){
            System.out.println("Something broke sending a message " + e);
        }
    }
    private void notification(Notification serverMessage) {
        System.out.println(serverMessage.getMessage());
    }
    private void loadGame(LoadGame serverMessage) {
        lastGameState = serverMessage.getGame().getChessGame();
        db.drawBoard(lastGameState);
    }
    private void Error(MyError serverMessage) {
        System.out.println(serverMessage.getErrorMessage());
    }
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public ChessGame getUpToDateGame() {
        return lastGameState;
    }
}