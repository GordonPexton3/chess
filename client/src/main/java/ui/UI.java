package ui;

import chess.ChessGame;
import model.GameData;
import server.MyRequest;
import server.MyResponse;

import java.util.Scanner;
import java.util.Vector;

public class UI {

    private ChessGame thisGame;
    private int gameID;
    private boolean logout;
    private String authToken;
    private final ServerFacade serverFacade;

    public UI(int port)  {
        this.serverFacade = new ServerFacade(port);
        preLogin();
    }

    private void preLogin() {
        while (!logout) {
            System.out.println("1. Help");
            System.out.println("2. Quit");
            System.out.println("3. Login");
            System.out.println("4. Register");
            System.out.printf("Type your numbers%n>>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            int result;
            try{
                result = Integer.parseInt(line);
            }catch(NumberFormatException e){
                result = 0;
            }
            if(result == 1){
                help();
            }else if(result == 2){
                break;
            }else if(result == 3){
                login();
            }else if(result == 4){
                register();
            }else{
                System.out.println("Input invalid. Try it again. This isn't hard. You can do it.");
            }
        }
    }

    private void help(){
        System.out.println("Dude, just select a number. It's not hard. Try it again.");
    }

    private void login() {
        while (!logout) {
            MyRequest req = new MyRequest();
            req.setAuthToken(this.authToken);
            Scanner scanner = new Scanner(System.in);
            System.out.printf("To login, please provide your username%n>>> ");
            req.setUsername(scanner.nextLine());
            System.out.printf("and your password%n>>> ");
            req.setPassword(scanner.nextLine());
            MyResponse resp = serverFacade.login(req);
            if(resp.getStatus() == 200){
                this.authToken = resp.getAuthToken();
                postLogin();
            }else{
                System.out.println(resp.getMessage());
            }
        }
    }

    private void register() {
        while (!logout) {
            MyRequest req = new MyRequest();
            Scanner scanner = new Scanner(System.in);
            System.out.printf("To login, please provide your username%n>>> ");
            req.setUsername(scanner.nextLine());
            System.out.printf("and your password%n>>> ");
            req.setPassword(scanner.nextLine());
            System.out.printf("and your email%n>>> ");
            req.setEmail(scanner.nextLine());
            MyResponse resp = serverFacade.register(req);
            if(resp.getStatus() == 200){
                this.authToken = resp.getAuthToken();
                postLogin();
            }else{
                System.out.println(resp.getMessage());
            }
        }
    }

    private void postLogin(){
        while (!logout) {
            MyRequest req = new MyRequest();
            System.out.println("1. Help");
            System.out.println("2. logout");
            System.out.println("3. Create Game");
            System.out.println("4. List Games");
            System.out.println("5. Join Games");
            System.out.println("6. Join Observer");
            System.out.printf("Type your numbers%n>>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            int result;
            try{
                result = Integer.parseInt(line);
            }catch(NumberFormatException e){
                result = 0;
            }
            if(result == 1){
                help();
            }else if(result == 2){
                req.setAuthToken(this.authToken);
                MyResponse resp = serverFacade.logout(req);
                if(resp.getStatus() == 200){
                    this.logout = true;
                }else{
                    System.out.println(resp.getMessage());
                }
            }else if(result == 3){
                createGame();
            }else if(result == 4){
                listGames();
            }else if(result == 5) {
                joinGame();
            }else if(result == 6){
                joinObserver();
            }else{
                System.out.println("Input invalid. Try it again. This isn't hard. You can do it.");
            }
        }
    }

    private void joinObserver() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        Scanner scanner = new Scanner(System.in);
        boolean gotGameID = false;
        while(!gotGameID){
            System.out.printf("What is the ID of the game you want to join as observer%n>>> ");
            try {
                req.setGameID(Integer.valueOf(scanner.nextLine()));
                gotGameID = true;
            } catch (NumberFormatException e) {
                System.out.println("please put in a 4 digit gameID.");
            }
        }
        MyResponse resp = serverFacade.joinGame(req);
        if(resp.getStatus() == 200){
            DrawBoard db = new DrawBoard();
        }else{
            System.out.println(resp.getMessage());
        }
    }

    private void joinGame() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        Scanner scanner = new Scanner(System.in);
        boolean gotGameID = false;
        while(!gotGameID){
            System.out.printf("What is the ID of the game you want to join%n>>> ");
            try {
                req.setGameID(Integer.valueOf(scanner.nextLine()));
                gotGameID = true;
            } catch (NumberFormatException e) {
                System.out.println("please put in a 4 digit gameID.");
            }
        }
        boolean gotPlayerColor = false;
        while(!gotPlayerColor){
            System.out.println("1. Join as While Player");
            System.out.println("2. Join as Black Player");
            int result;
            try{
                result = Integer.valueOf(scanner.nextLine());
            }catch(NumberFormatException e){
                result = 0;
            }
            if(result == 1){
                req.setPlayerColor("WHITE");
                gotPlayerColor = true;
            }else if(result == 2){
                req.setPlayerColor("BLACK");
                gotPlayerColor = true;
            }else{
                System.out.println("Please put in 1 or 2");
            }
        }
        MyResponse resp = serverFacade.joinGame(req);
        if(resp.getStatus() == 200){
            DrawBoard db = new DrawBoard();
        }else{
            System.out.println(resp.getMessage());
        }
    }

    private void listGames() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        MyResponse resp = serverFacade.listGames(req);
        if(resp.getStatus() == 200){
            Vector<GameData> games = resp.getGames();
            System.out.println("These are the games");
            for(GameData game: games){
                System.out.println(game.getGameName() + ", " + game.getGameID());
            }
        }else{
            System.out.println(resp.getMessage());
        }
    }

    private void createGame() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        Scanner scanner = new Scanner(System.in);
        System.out.printf("What is your game name%n>>> ");
        req.setGameName(scanner.nextLine());
        MyResponse resp = serverFacade.createGame(req);
        if(resp.getStatus() == 200){
            this.gameID = resp.getGameID();
            DrawBoard db = new DrawBoard();
            this.thisGame = new ChessGame();
        }else{
            System.out.println(resp.getMessage());
        }
    }
}
