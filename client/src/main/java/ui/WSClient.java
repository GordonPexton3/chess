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
    private final Gson gson = new Gson();
    private static boolean response = false;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {

                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()){
                    case ServerMessage.ServerMessageType.ERROR -> Error(message);
                    case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(message);
                    case ServerMessage.ServerMessageType.NOTIFICATION -> notification(message);
                }
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
    private void notification(String message) {
        Notification notification = gson.fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }
    private void loadGame(String message) {
        LoadGame loadGame = gson.fromJson(message, LoadGame.class);
        lastGameState = loadGame.getGame().getChessGame();
    }
    private void Error(String message) {
        MyError error = gson.fromJson(message, MyError.class);
        System.out.println(error.getErrorMessage());
    }
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public ChessGame getLastGameState() {
        return lastGameState;
    }
}