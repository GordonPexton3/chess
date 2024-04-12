package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.MyError;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class GamePlay implements ServerMessageObserver {
    private final DrawBoard db = new DrawBoard();
    private final Gson gson = new Gson();
    private final Scanner scanner = new Scanner(System.in);
    private WSClient ws;
    private final String authToken;
    private final int gameID;
    private ChessGame lastGameState;
    private final ChessGame.TeamColor playerColor;

    public GamePlay(int gameID, String authToken, ChessGame.TeamColor playerColor) {
        try{
            ws = new WSClient(this);
        }catch(Exception e){
            System.out.println("The websocket didn't connect: " + e.getMessage());
        }
        this.gameID = gameID;
        this.authToken = authToken;
        this.playerColor = playerColor;
        indicateYouJoined();
        run();
    }

    private void indicateYouJoined() {
        UserGameCommand command;
        if(playerColor == null){
            command = new JoinObserver(authToken, gameID, null);
        }else{
            command = new JoinPlayer(authToken, gameID, playerColor);
        }
        send(command);
    }

    private void run(){
        boolean leave = false;
        System.out.print("Help (h)\n>>> ");
        while(!leave) {
            String response = scanner.nextLine();
            switch (response) {
                case "h" -> System.out.println("""
                    you can type any of the following ...
                    Redraw
                    Leave
                    Make Move
                    Resign
                    Show Moves""");
                case "Redraw" -> {  db.drawBoard(lastGameState, playerColor);
                                    System.out.print(">>> ");}
                case "Leave" -> leave = leave();
                case "Make Move" -> makeMoves();
                case "Resign" -> resign();
                case "Show Moves" -> highlightMoves();
                default -> System.out.print("Input invalid. Try it again. This isn't hard. You can do it.\n>>> ");
            }
        }
    }

    private boolean leave() {
        UserGameCommand command = new Leave(authToken,gameID);
        send(command);
        return true;
    }

    private void resign(){
        db.drawBoard(lastGameState, playerColor);
        UserGameCommand command = new Resign(authToken,gameID);
        send(command);
    }

    private void highlightMoves() {
        try{
            System.out.println("What is the position of the piece");
            String piecePositionString = scanner.nextLine();
            List<Integer> piecePositionList = parseInputToCoordinates(piecePositionString);
            ChessPosition piecePosition = new ChessPosition(piecePositionList.get(0), piecePositionList.get(1));
            db.drawMovesOnBoard(lastGameState, piecePosition);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void makeMoves() {
        try{
            System.out.println("Starting Coordinate");
            String startingCoordinateStr = scanner.nextLine();
            List<Integer> startCoordinates = parseInputToCoordinates(startingCoordinateStr);
            System.out.println("Ending Coordinate");
            String endingCoordinateStr = scanner.nextLine();
            List<Integer> endingCoordinate = parseInputToCoordinates(endingCoordinateStr);
            ChessPosition startingPosition = new ChessPosition(startCoordinates.get(0), startCoordinates.get(1));
            ChessPosition endPosition = new ChessPosition(endingCoordinate.get(0), endingCoordinate.get(1));
            ChessMove move = new ChessMove(startingPosition, endPosition, null);
            UserGameCommand command = new MakeMove(authToken,gameID,move);
            send(command);
        }catch(Exception e){
            System.out.print(e.getMessage());
        }
    }



    private List<Integer> parseInputToCoordinates(String startingCoordinateStr) throws Exception {
        char[] charArray = startingCoordinateStr.toCharArray();
        if(charArray.length != 2){
            throw new Exception("Your input is invalid, please put a row and a column with no spaces\n>>> ");
        }
        Integer startRow = numCharToInt.get(charArray[0]);
        Integer startCol = charToInt.get(charArray[1]);
        if(startRow == null || startCol == null){
            throw new Exception("Your input is invalid, please put a row and a column with no spaces\n>>> ");
        }
        return Arrays.asList(startRow, startCol);
    }

    private void send(UserGameCommand command){
        ws.send(command);
        try{sleep(500);}catch(Exception e){
            System.out.println("Timer Problem");
        }
    }
    private final Map<Character, Integer> numCharToInt = Map.of(
            '1',1,
            '2',2,
            '3',3,
            '4',4,
            '5',5,
            '6',6,
            '7',7,
            '8',8
    );
    private final Map<Character, Integer> charToInt = Map.of(
            'a',1,
            'b',2,
            'c',3,
            'd',4,
            'e',5,
            'f',6,
            'g',7,
            'h',8
    );

    @Override
    public void notify(ServerMessage serverMessage, String jsonMessage) {
        switch (serverMessage.getServerMessageType()){
            case ServerMessage.ServerMessageType.ERROR -> Error(jsonMessage);
            case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(jsonMessage);
            case ServerMessage.ServerMessageType.NOTIFICATION -> notification(jsonMessage);
        }
    }

    private void notification(String message) {
        Notification notification = gson.fromJson(message, Notification.class);
        System.out.print("Notification: " + notification.getMessage() + "\n>>> ");
    }
    private void loadGame(String message) {
        LoadGame loadGame = gson.fromJson(message, LoadGame.class);
        lastGameState = loadGame.getGame().getChessGame();
        System.out.println("\n");
        db.drawBoard(lastGameState, playerColor);
        System.out.print(">>> ");
    }
    private void Error(String message) {
        MyError error = gson.fromJson(message, MyError.class);
        System.out.print(error.getErrorMessage() + "\n>>> ");
    }
}
