package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GamePlay {
    public Session session;
    private DrawBoard db = new DrawBoard();
    private Scanner scanner = new Scanner(System.in);
    private int gameID;
    private String teamColor;
    private String authToken;

    public GamePlay(int gameID, String authToken) {
        configureWSConnection();
//        this.teamColor = teamColor;
        this.authToken = authToken;
        this.gameID = gameID;
        run();
    }

    private void run(){
//        db.drawBoard(chessGame);
        boolean leave = false;
        boolean resign = false;
        System.out.print("Help (h) ");
        while(!leave && !resign) {
            System.out.printf("%n>>> ");
            String response = scanner.nextLine();
            if (response.equals("h")) {
                System.out.println("you can type any of the following ...");
                System.out.println("Redraw Chess Board");
                System.out.println("Leave");
                System.out.println("Make Move");
                System.out.println("Resign");
                System.out.println("Highlight Legal Moves");
            } else if (response.equals("Redraw Chess Board")) {
//                db.drawBoard(chessGame);
            } else if (response.equals("Leave")) {
                leave = true;
            } else if (response.equals("Make Move")) {
                makeMoves();
            } else if (response.equals("Resign")) {
                resign = true;
            } else if (response.equals("Highlight Legal Moves")) {
                highlightMoves();
            } else {
                System.out.println("Input invalid. Try it again. This isn't hard. You can do it.");
            }
        }
    }

    private void highlightMoves() {
        System.out.println("What is the position of the piece");
        String piecePositionString = scanner.nextLine();
        List<Integer> piecePositionList = parseInputToCoordinates(piecePositionString);
        ChessPosition piecePosition = new ChessPosition(piecePositionList.get(0), piecePositionList.get(1));
//        db.drawMovesOnBoard(chessGame, piecePosition);
    }

    private int makeMoves() {
        System.out.println("Starting Coordinate");
        String startingCoordinateStr = scanner.nextLine();
        List<Integer> startCoordinates = parseInputToCoordinates(startingCoordinateStr);
        if (startCoordinates == null) { return 0; }

        System.out.println("Ending Coordinate");
        String endingCoordinateStr = scanner.nextLine();
        List<Integer> endingCoordinate = parseInputToCoordinates(endingCoordinateStr);
        if (endingCoordinate == null) { return 0;}

        ChessPosition startingPosition = new ChessPosition(startCoordinates.get(0), startCoordinates.get(1));
        ChessPosition endPosition = new ChessPosition(endingCoordinate.get(0), endingCoordinate.get(1));
        ChessMove move = new ChessMove(startingPosition, endPosition, null);
        UserGameCommand command = new MakeMove(authToken,gameID,move);
        send(command);
        return 0;
    }

    private List<Integer> parseInputToCoordinates(String startingCoordinateStr){
        char[] charArray = startingCoordinateStr.toCharArray();
        if(charArray.length != 2){
            System.out.println("Your input is invalid, please put a row and a column with no spaces");
            return null;
        }
        Integer startRow = numCharToInt.get(charArray[0]);
        Integer startCol = charToInt.get(charArray[1]);
        if(startRow == null || startCol == null){
            System.out.println("Your input is invalid, please put a row and a column with no spaces");
            return null;
        }
        return Arrays.asList(startRow, startCol);
    }

    private void configureWSConnection() {
        try{
            URI uri = new URI("ws://localhost:8080/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {

                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()){
                    case ServerMessage.ServerMessageType.ERROR -> ERROR(serverMessage);
                    case ServerMessage.ServerMessageType.LOAD_GAME -> loadGame(serverMessage);
                    case ServerMessage.ServerMessageType.NOTIFICATION -> notification(serverMessage);
                }
            });
        }catch (Exception e){
            System.out.println("Something broke in this class with connecting to the server");
        }
    }

    private void notification(ServerMessage serverMessage) {

    }

    private void loadGame(ServerMessage serverMessage) {

    }

    private void ERROR(ServerMessage serverMessage) {

    }

    public void send(UserGameCommand command) {
        try{
            String strMsg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(strMsg);
        }catch(Exception e){
            System.out.println("Something broke sending a message " + e);
        }
    }

    private final Map<Character, Integer> numCharToInt = Map.of(
            '1',8,
            '2',7,
            '3',6,
            '4',5,
            '5',4,
            '6',3,
            '7',2,
            '8',1
    );
    private final Map<Character, Integer> charToInt = Map.of(
            'a',8,
            'b',7,
            'c',6,
            'd',5,
            'e',4,
            'f',3,
            'g',2,
            'h',1
    );
}
