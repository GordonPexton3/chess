package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class WSClient extends Endpoint {
    public Session session;
    public boolean response;

    public WSClient(ServerMessageObserver smo) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                smo.notify(serverMessage, message);
            }
        });
    }

    public void send(UserGameCommand command) {
        response = false;
        try{
            String strMsg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(strMsg);
            Thread.sleep(500);
        }catch(Exception e){
            System.out.println("Something broke sending a message " + e);
        }
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}