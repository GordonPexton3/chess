package server;

import ServerCommunicationClasses.MyRequest;
import ServerCommunicationClasses.MyResponse;
import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
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

        UserGameCommand command = gson.fromJson(message,UserGameCommand.class);
        switch (command.getCommandType()){
            case UserGameCommand.CommandType.JOIN_OBSERVER -> joinObserver(session, message);
            case UserGameCommand.CommandType.JOIN_PLAYER -> joinPlayer(session, message);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(session, message);
            case UserGameCommand.CommandType.LEAVE -> leave(session, message);
            case UserGameCommand.CommandType.RESIGN -> resign(session, message);
        }
    }

    private void joinObserver(Session session, String message) {
        System.out.println("Join Observer");
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
                throw new Exception("that is not a valid gameID");
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
            System.out.println("Join Observer Error");
            response = new MyError("Error in joinObserver" + e.getMessage());
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            } catch (IOException a) {
                System.out.println("Problem in WSSserver::joinPlayer " + a);
            }
        }
    }

    private void joinPlayer(Session session, String message) {
        System.out.println("Join Player");
        System.out.println(message);
        ServerMessage response;
        JoinPlayer command;
        try{
            command = gson.fromJson(message, JoinPlayer.class);
            String username;
            if (sessionToUsername.get(session) == null){
                if(command.getAuthString() == null){
                    justSendHimAGame(session, command);
                    System.out.println("This is where the error happens");
                    throw new NullPointerException();
                }
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
            boolean userSameColorAsInGameData = false;
            try {
                loadedGame = GameInteractions.getGame(req);
            } catch (Exception e) {
                throw new Exception("That is a bad GameID");
            }
            try{
                switch (command.getPlayerColor()) {
                    case ChessGame.TeamColor.WHITE -> userSameColorAsInGameData = username.equals(loadedGame.getWhiteUsername());
                    case ChessGame.TeamColor.BLACK -> userSameColorAsInGameData = username.equals(loadedGame.getBlackUsername());
                }
            }catch(NullPointerException e){
                throw new Exception("This shouldn't have happened, you should exist as the player.");
            }
            if (userSameColorAsInGameData && loadedGame != null) {
                addPlayer(command.getGameID(), session, username);
                // Server sends a LOAD_GAME message back to the root client.
                response = new LoadGame(loadedGame);
                try {
                    session.getRemote().sendString(new Gson().toJson(response));
                } catch (IOException e) {
                    System.out.println("Problem in WSSserver::joinPlayer " + e);
                }
                // Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
                response = new Notification(" user " + username + " joined as " + command.getPlayerColor().toString());
                sendToAllButRoot(response, command.getGameID(), session);
            }else{
                throw new Exception("you cannot join as that color, it is already taken");
            }
        }catch(NullPointerException e){
            System.out.println("maybe this worked");
        }catch(Exception e){
            System.out.println("Join Player Error");
            response = new MyError("Error in joinPlayer: " + e.getMessage());
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            } catch (IOException a) {
                System.out.println("Problem in WSSserver::joinPlayer " + a);
            }
        }
    }

    private void justSendHimAGame(Session session, JoinPlayer command){
        GameData loadedGame;
        ServerMessage response;
        try{
            loadedGame = GameInteractions.desperateGetGame(command.getGameID());
            response = new LoadGame(loadedGame);
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            } catch (IOException e) {
                System.out.println("Problem in WSSserver::joinPlayer " + e);
            }
        }catch(DataAccessException e){
            System.out.println("Problem in WSServer::justSendHimAGame");
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
        sessionToUsername.putIfAbsent(session, username);
    }

    private void makeMove(Session session, String message)  {
        System.out.println("Make Move");
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
                if(loadedGame.getChessGame().gameOver){
                    throw new Exception("You cannot make a move when the game is over");
                }
            } catch (Exception e) {
                throw new Exception("That is a bad GameID");
            }
            if(!username.equals(loadedGame.getBlackUsername()) && !username.equals(loadedGame.getWhiteUsername())){
                throw new Exception("You cannot make a move as an observer");
            }
            ChessPiece pieceAtPosition = loadedGame.getChessGame().getBoard().getPiece(command.getMove().getStartPosition());
            if(pieceAtPosition == null){
                throw new Exception("That is an invalid move, select a space with a piece in it");
            }
            boolean userIsSameColorAsPieceBeingMoved = false;
            switch(pieceAtPosition.getTeamColor()){
                case ChessGame.TeamColor.WHITE -> userIsSameColorAsPieceBeingMoved = username.equals(loadedGame.getWhiteUsername());
                case ChessGame.TeamColor.BLACK -> userIsSameColorAsPieceBeingMoved = username.equals(loadedGame.getBlackUsername());
            }
            if (!userIsSameColorAsPieceBeingMoved) {
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
            ChessGame.TeamColor otherPlayerColor = loadedGame.getChessGame().getTeamTurn();
            String otherPlayerUsername = "";
            switch (otherPlayerColor){
                case BLACK -> otherPlayerUsername = loadedGame.getBlackUsername();
                case WHITE -> otherPlayerUsername = loadedGame.getWhiteUsername();
            }
            if(loadedGame.getChessGame().isInCheck(otherPlayerColor)){
                msg = "Player " + otherPlayerUsername + " is now in check";
                response = new Notification(msg);
                sendToAllButRoot(response, command.getGameID(), session);
            }
            if(loadedGame.getChessGame().isInCheckmate(otherPlayerColor)){
                msg = "Player " + otherPlayerUsername + " is now in check mate";
                response = new Notification(msg);
                sendToAllButRoot(response, command.getGameID(), session);
            }
        }catch(Exception e){
            System.out.println("Make Move Error");
            response = new MyError("Error in makeMove: " + e.getMessage());
            try {
                session.getRemote().sendString(new Gson().toJson(response));
            }catch(IOException a){
                System.out.println("There is a problem with casting your error to a json and returning it: " + a);
            }
        }
    }

    private void leave(Session session, String message) {
        System.out.println("Leave");
        ServerMessage response;
        try{
            Leave command = gson.fromJson(message, Leave.class);
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
            System.out.println("Leave Error");
            response = new MyError("Error in leave: " + e.getMessage());
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
        System.out.println("Resign");
        ServerMessage response;
        try{
            Resign command = gson.fromJson(message, Resign.class);
            if(gameIDToSessions.get(command.getGameID()) == null){
                throw new Exception("The game is already over");
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
            response = new Notification("user " + username + " resigned from game with ID " + command.getGameID());
            sendToAllRelated(response,command.getGameID());
            removeGame(command.getGameID());
        }catch(Exception e){
            System.out.println("Resign Error");
            response = new MyError("Error in resign: " + e.getMessage());
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
            System.out.println("You broke something in sendToAllRelated in WSServer " + e.getMessage());
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