package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import server.MyRequest;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GamePlay implements ServerMessageObserver {
    private final DrawBoard db = new DrawBoard();
    private final Scanner scanner = new Scanner(System.in);
    private WSClient ws;
    private final String authToken;
    private final int gameID;
    private final MyRequest req = new MyRequest();
    private final String username;
    private final ChessGame.TeamColor playerColor;

    public GamePlay(int gameID, String authToken, String username, ChessGame.TeamColor playerColor) {
        try{
            ws = new WSClient();
        }catch(Exception e){
            System.out.println("The websocket didn't connect: " + e);
        }
        this.gameID = gameID;
        this.authToken = authToken;
        req.setAuthToken(authToken);
        req.setGameID(gameID);
        this.username = username;
        this.playerColor = playerColor;
        indicateYouJoined();
        run();
    }

    private void indicateYouJoined() {
        UserGameCommand command;
        if(playerColor == null){
            // joined as observer
            command = new JoinObserver(authToken, gameID, null);
        }else{
            command = new JoinPlayer(authToken, gameID, playerColor);
        }
        ws.send(command);
        while(ws.getLastGameState() == null){};
        db.drawBoard(ws.getLastGameState());
    }

    private void run(){
        boolean leave = false;
        boolean resign = false;
        System.out.print("Help (h) ");
        while(!leave && !resign) {
            System.out.printf("%n>>> ");
            String response = scanner.nextLine();
            switch (response) {
                case "h" -> {
                    System.out.println("you can type any of the following ...");
                    System.out.println("Redraw Chess Board");
                    System.out.println("Leave");
                    System.out.println("Make Move");
                    System.out.println("Resign");
                    System.out.println("Highlight Legal Moves");
                }
                case "Redraw" -> System.out.println("will redraw");
                case "Leave" -> leave = true;
                case "Make Move" -> makeMoves();
                case "Resign" -> resign = resign();
                case "Highlight Legal Moves" -> highlightMoves();
                default -> System.out.println("Input invalid. Try it again. This isn't hard. You can do it.");
            }
        }
    }

    private boolean resign(){
        UserGameCommand command = new Resign(authToken,gameID);
        ws.send(command);
        return true;
    }

    private void highlightMoves() {
        System.out.println("What is the position of the piece");
        String piecePositionString = scanner.nextLine();
        List<Integer> piecePositionList = parseInputToCoordinates(piecePositionString);
        ChessPosition piecePosition = new ChessPosition(piecePositionList.get(0), piecePositionList.get(1));
        db.drawMovesOnBoard(ws.getLastGameState(), piecePosition);
    }

    private void makeMoves() {
        try{
            System.out.println("Starting Coordinate");
            String startingCoordinateStr = scanner.nextLine();
            List<Integer> startCoordinates = parseInputToCoordinates(startingCoordinateStr);
            if (startCoordinates == null) { throw new Exception(); }

            System.out.println("Ending Coordinate");
            String endingCoordinateStr = scanner.nextLine();
            List<Integer> endingCoordinate = parseInputToCoordinates(endingCoordinateStr);
            if (endingCoordinate == null) { throw new Exception();}

            ChessPosition startingPosition = new ChessPosition(startCoordinates.get(0), startCoordinates.get(1));
            ChessPosition endPosition = new ChessPosition(endingCoordinate.get(0), endingCoordinate.get(1));
            ChessMove move = new ChessMove(startingPosition, endPosition, null);
            UserGameCommand command = new MakeMove(authToken,gameID,move);
            ws.send(command);
        }catch(Exception e){
            System.out.println("Problem in makeMoves GamePlay");
        }
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

    @Override
    public void notify(ServerMessage message, String jsonMessage) {

    }
}
