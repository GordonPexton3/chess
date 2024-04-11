package server;

import chess.ChessGame;
import chess.ChessPiece;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebSocket
public class WSServer {
    private final Map<Integer, List<Session>> gameIDToSessions = new HashMap<>();
    private final Map<Session, String> sessionToUsername = new HashMap<>();
    private final Gson gson = new Gson();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(gameIDToSessions);
        UserGameCommand command = gson.fromJson(message,UserGameCommand.class);
        switch (command.getCommandType()){
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver(session, message);
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer(session, message);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(session, message);
            case UserGameCommand.CommandType.LEAVE -> leave(session, message);
            case UserGameCommand.CommandType.RESIGN -> resign(session, message);
        }
        System.out.println(gameIDToSessions);
    }

    private void joinObserver(Session session, String message) {
        ServerMessage response;
        try{
            JoinObserver command = gson.fromJson(message, JoinObserver.class);

            String username;
            if (sessionToUsername.get(session) == null){
                try{
                    username = Authentications.getUsername(command.getAuthString());
                }catch(Exception e){
                    throw new Exception("That is a bad auth token");
                }
            }else{
                username = sessionToUsername.get(session);
            }
            // Load Game back to root client
            MyRequest req = new MyRequest();
            req.setAuthToken(command.getAuthString());
            req.setUsername(username);
            req.setGameID(command.getGameID());
            GameData gameData;
            try{
                gameData = GameInteractions.getGame(req);
            }catch(Exception e){
                throw new Exception("Error: that is not a valid gameID");
            }
            // send game back to root
            response = new LoadGame(gameData);
            session.getRemote().sendString(new Gson().toJson(response));

            // message all related user joined as observer
            String msg = "Player " + username + " joined game " + command.getGameID() + " as observer";
            response = new Notification(msg);
            addPlayer(command.getGameID(),session, username);
            sendToAllButRoot(response, command.getGameID(), session);
        }catch(Exception e){
            response = new MyError("Error: " + e);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            } catch (IOException a) {
                System.out.println("Problem in WSSserver::joinPlayer " + a);
            }
        }
    }

    private void joinPlayer(Session session, String message) {
        ServerMessage response;
        try{
            JoinPlayer command = gson.fromJson(message, JoinPlayer.class);
            String username;
            if (sessionToUsername.get(session) == null){
                try{
                    username = Authentications.getUsername(command.getAuthString());
                }catch(Exception e){
                    throw new Exception("That is a bad auth token");
                }
            }else{
                username = sessionToUsername.get(session);
            }
            MyRequest req = new MyRequest();
            req.setAuthToken(command.getAuthString());
            req.setUsername(username);
            req.setGameID(command.getGameID());
            GameData loadedGame;
            String usernameInThatPlace = null;
            try {
                loadedGame = GameInteractions.getGame(req);
                if(loadedGame == null){
                    throw new Exception("Could not get game in joinPlayer");
                }else{
                    switch (command.getPlayerColor()) {
                        case ChessGame.TeamColor.WHITE -> usernameInThatPlace = loadedGame.getWhiteUsername();
                        case ChessGame.TeamColor.BLACK -> usernameInThatPlace = loadedGame.getBlackUsername();
                    }
                }
            } catch (Exception e) {
                throw new Exception("That is a bad GameID");
            }
            if (usernameInThatPlace.equals(username) && loadedGame != null) {
                addPlayer(command.getGameID(), session, username);
                // Server sends a LOAD_GAME message back to the root client.
                response = new LoadGame(loadedGame);
                try {
                    session.getRemote().sendString(new Gson().toJson(response));
                } catch (IOException e) {
                    System.out.println("Problem in WSSserver::joinPlayer " + e);
                }
                // Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
                response = new Notification(" user " + username + " joined as " + req.getPlayerColor());
                sendToAllButRoot(response, command.getGameID(), session);
            }else{
                throw new Exception("you cannot join as that color, it is already taken");
            }

        }catch(Exception e){
            System.out.println(e);
            response = new MyError("Error: " + e);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            } catch (IOException a) {
                System.out.println("Problem in WSSserver::joinPlayer " + a);
            }
        }
    }

    private void addPlayer(int gameID, Session session, String username) {
        if(gameIDToSessions.get(gameID) == null){
            List<Session> newList = new ArrayList<>();
            newList.add(session);
            gameIDToSessions.put(gameID,newList);
        }else if(!gameIDToSessions.get(gameID).contains(session)) {
            gameIDToSessions.get(gameID).add(session);
        }
    }

    private void makeMove(Session session, String message)  {
        ServerMessage response;
        try{
            MakeMove command = gson.fromJson(message, MakeMove.class);
            String username;
            try{
                username = Authentications.getUsername(command.getAuthString());
            }catch(Exception e){
                throw new Exception("That is a bad auth token");
            }
            MyRequest req = new MyRequest();
            req.setAuthToken(command.getAuthString());
            req.setUsername(username);
            req.setGameID(command.getGameID());
            GameData loadedGame;
            try {
                loadedGame = GameInteractions.getGame(req);
                if(loadedGame != null){
                    if(loadedGame.getChessGame().gameOver){
                        throw new Exception("You cannot make a move when the game is over");
                    }
                }
            } catch (Exception e) {
                throw new Exception("That is a bad GameID");
            }
            if(!username.equals(loadedGame.getBlackUsername()) && !username.equals(loadedGame.getWhiteUsername())){
                throw new Exception("You cannot make a move as an observer");
            }
            ChessPiece pieceAtPosition = loadedGame.getChessGame().getBoard().getPiece(command.getMove().getStartPosition());
            String pieceUsername = null;
            switch(pieceAtPosition.getTeamColor()){
                case ChessGame.TeamColor.WHITE -> pieceUsername = loadedGame.getWhiteUsername();
                case ChessGame.TeamColor.BLACK -> pieceUsername = loadedGame.getBlackUsername();
            }
            if (!pieceUsername.equals(username)) {
                throw new Exception("You cannot move an opponents piece");
            }
            GameData updatedGameData;
            try {
                updatedGameData = GameInteractions.makeMove(command.getGameID(), command.getMove());
            }catch(InvalidMoveException e){
                throw new Exception("That move is invalid");
            }
            response = new LoadGame(updatedGameData);
            sendToAllRelated(response, command.getGameID());

            String msg = "Player " + username + " made move " + command.getMove().toString();
            response = new Notification(msg);
            sendToAllButRoot(response, command.getGameID(), session);

        }catch(Exception e){
            response = new MyError("Error: " + e);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void leave(Session session, String message) {
        ServerMessage response;
        try{
            Leave command = gson.fromJson(message, Leave.class);
            if(!gameIDToSessions.get(command.getGameID()).contains(session)){
                throw new Exception("You cannot leave twice");
            }
            String username;
            try{
                username = Authentications.getUsername(command.getAuthString());
            }catch(Exception e){
                throw new Exception("That is a bad auth token");
            }
            response = new Notification(username + " left the game with ID " + command.getGameID());
            sendToAllRelated(response,command.getGameID());
            removePlayer(command.getGameID(), session);
        }catch(Exception e){
            response = new MyError("Error: " + e);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void removePlayer(int gameID, Session session) {
        gameIDToSessions.get(gameID).remove(session);
        sessionToUsername.remove(session);
    }

    private void resign(Session session, String message) {
        ServerMessage response;
        try{
            Resign command = gson.fromJson(message, Resign.class);
            if(gameIDToSessions.get(command.getGameID()) == null){
                throw new Exception("You cannot resign twice");
            }
            String username;
            try{
                username = Authentications.getUsername(command.getAuthString());
            }catch(Exception e){
                throw new Exception("That is a bad auth token");
            }
            MyRequest req = new MyRequest();
            req.setUsername(username);
            req.setGameID(command.getGameID());
            req.setAuthToken(command.getAuthString());
            MyResponse resp = GameInteractions.endGame(req);
            if(resp.getStatus() != 200){
                throw new Exception(resp.getMessage());
            }
            response = new Notification(username + " resigned from game with ID " + command.getGameID());
            sendToAllRelated(response,command.getGameID());
            removeGame(command.getGameID());
//            removePlayer(command.getGameID(), session);
        }catch(Exception e){
            response = new MyError("Error: " + e);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void removeGame(int gameID) {
        gameIDToSessions.remove(gameID);
    }

    private void sendToAllRelated(ServerMessage msg, int gameID){
        try{
            for(Session session: gameIDToSessions.get(gameID)){
                session.getRemote().sendString(new Gson().toJson(msg));
            }
        }catch(IOException e){
            System.out.println("You broke something in sendToAllRelated in WSServer " + e);
        }
    }

    private void sendToAllButRoot(ServerMessage msg, int gameID, Session rootSession) {
        try{
            for(Session session: gameIDToSessions.get(gameID)){
                if(!session.equals(rootSession)){
                    session.getRemote().sendString(new Gson().toJson(msg));
                }
            }
        }catch(IOException e){
            System.out.println("You broke something in sendToAllButRoot in WSServer " + e);
        }
    }
}