package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class WSClient extends Endpoint {
    public Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {

            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            switch (serverMessage.getServerMessageType()){
                case ServerMessage.ServerMessageType.ERROR -> Error(serverMessage);
                case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(serverMessage);
                case ServerMessage.ServerMessageType.NOTIFICATION -> notification(serverMessage);
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

    private void notification(ServerMessage serverMessage) {

    }

    private void loadGame(ServerMessage serverMessage) {

    }

    private void Error(ServerMessage serverMessage) {

    }


    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}