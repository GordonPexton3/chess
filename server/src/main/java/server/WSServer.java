package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.MyError;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class WSServer {
    private Map<Integer, List<Session>> sessions = new HashMap<>();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message,UserGameCommand.class);
        switch (command.getCommandType()){
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver(session, (JoinObserver) command);
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer(session, (JoinPlayer) command);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(session, (MakeMove) command);
            case UserGameCommand.CommandType.LEAVE -> leave(session, (Leave) command);
            case UserGameCommand.CommandType.RESIGN -> resign(session, (Resign) command);
        }
    }

    private void joinObserver(Session session, JoinObserver command) {
        ServerMessage response;
        try{
            MyRequest req = new MyRequest();
            req.setGameID(command.getGameID());
            req.setAuthToken(command.getAuthString());
            MyResponse resp = GameInteractions.joinGame(req);
            if (resp.getStatus() != 200){
                throw new Exception();
            }
            GameData updatedGameData = GameInteractions.getGame(req);
            if(updatedGameData == null){
                throw new Exception();
            }
            String msg = command.getUserID() + "joined game " + command.getUserID();
            response = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,msg);
            sendToAllButRoot(response, command.getGameID(), session);

            response = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,updatedGameData);
            sendToAllRelated(response, command.getGameID());

        }catch(Exception e){
            response = new MyError(ServerMessage.ServerMessageType.ERROR,e.toString());
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void joinPlayer(Session session, JoinPlayer command) {
    }

    private void makeMove(Session session, MakeMove command)  {
        ServerMessage response;
        try{
            GameData updatedGameData = GameInteractions.makeMove(command.getGameID(), command.getMove());

            String msg = "Player made move" + command.getMove().toString();
            response = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,msg);
            sendToAllButRoot(response, command.getGameID(), session);

            response = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,updatedGameData);
            sendToAllRelated(response, command.getGameID());

        }catch(InvalidMoveException e){
            response = new MyError(ServerMessage.ServerMessageType.ERROR,e.toString());
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void leave(Session session, Leave command) {

    }

    private void resign(Session session, Resign command) {

    }

    private void sendToAllRelated(ServerMessage msg, int gameID){
        try{
            for(Session session: sessions.get(gameID)){
                session.getRemote().sendString(new Gson().toJson(msg));
            }
        }catch(IOException e){
            System.out.println("You broke something in sendToAllRelated in WSServer " + e);
        }
    }

    private void sendToAllButRoot(ServerMessage msg, int gameID, Session rootSession) {
        try{
            for(Session session: sessions.get(gameID)){
                if(!session.equals(rootSession)){
                    session.getRemote().sendString(new Gson().toJson(msg));
                }
            }
        }catch(IOException e){
            System.out.println("You broke something in sendToAllButRoot in WSServer " + e);
        }
    }
}