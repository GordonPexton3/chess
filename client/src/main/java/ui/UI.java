package ui;

import chess.ChessGame;
import model.GameData;
import model.MyRequest;
import model.MyResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class UI {
    private final Map<Integer, GameData> games = new HashMap<>();
    private boolean logout;
    private String authToken;
    private ServerFacade serverFacade;
    private ChessGame.TeamColor playerColor;
    private final Scanner scanner = new Scanner(System.in);

    public void run(int port){
        this.serverFacade = new ServerFacade(port);
        preLogin();
    }

    private void preLogin() {
        boolean quit = false;
        while (!quit) {
            this.logout = false;
            System.out.print("""
                               1. Help
                               2. Quit
                               3. Login
                               4. Register
                               >>>
                               """);
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
                quit = true;
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
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
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

    private void register() {
        MyRequest req = new MyRequest();
        System.out.printf("To register, please provide your username%n>>> ");
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

    private void postLogin(){
        populateGamesList();
        while (!logout) {
            MyRequest req = new MyRequest();
            System.out.println("""
                    1. Help
                    2. logout
                    3. Create Game
                    4. List Games
                    5. Join Games
                    6. Join Observer
                    >>>""");
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

    private void joinObserver(){
        populateGamesList();
        try {
            MyRequest req = new MyRequest();
            req.setAuthToken(this.authToken);

            System.out.printf("What is the number of the game you want to join as observer%n>>> ");
            int selectedGameNumber = Integer.parseInt(scanner.nextLine());
            GameData selectedGameData = this.games.get(selectedGameNumber);
            req.setGameID(selectedGameData.getGameID());
            MyResponse resp = serverFacade.joinGame(req);
            if(resp.getStatus() == 200){
                new GamePlay(selectedGameData.getGameID(), authToken, playerColor);
                try{
                    sleep(500);
                }catch (InterruptedException e) {
                    System.out.println("TImer problems");
                }
            }else{
                System.out.println(resp.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("please put in a game number as appears in the game list");
        } catch (NullPointerException e){
            System.out.println("That number does not refer to an existing game.");
        }
    }

    private void joinGame() {
        boolean once = true;
        while(once){
            once = false;
            MyRequest req = new MyRequest();
            req.setAuthToken(this.authToken);
            System.out.printf("What is the number of the game you want to join as player%n>>> ");
            try {
                req.setGameID(this.games.get(Integer.valueOf(scanner.nextLine())).getGameID());
            } catch (NumberFormatException e) {
                System.out.println("please put in a game number as appears in the game list");
                break;
            } catch (NullPointerException e){
                System.out.println("That number does not refer to an existing game.");
                break;
            }
            boolean gotPlayerColor = false;
            while(!gotPlayerColor){
                System.out.println("1. Join as While Player");
                System.out.println("2. Join as Black Player");
                int result;
                try{
                    result = Integer.parseInt(scanner.nextLine());
                }catch(NumberFormatException e){
                    result = 0;
                }
                if(result == 1){
                    req.setPlayerColor("WHITE");
                    playerColor = ChessGame.TeamColor.WHITE;
                    gotPlayerColor = true;
                }else if(result == 2){
                    req.setPlayerColor("BLACK");
                    playerColor = ChessGame.TeamColor.BLACK;
                    gotPlayerColor = true;
                }else{
                    System.out.println("Please put in 1 or 2");
                }
            }
            MyResponse resp = serverFacade.joinGame(req);
            if(resp.getStatus() == 200){
                new GamePlay(req.getGameID(), authToken, playerColor);
                try{
                    sleep(500);
                }catch (InterruptedException e) {
                    System.out.println("TImer Problems");
                }
            }else{
                System.out.println(resp.getMessage());
            }
        }
    }

    private void listGames() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        MyResponse resp = serverFacade.listGames(req);
        if(resp.getStatus() == 200){
            Vector<GameData> games = resp.getGames();
            System.out.println("These are the games");
            int enumerating = 1;
            this.games.clear();
            for(GameData game: games){
                System.out.println(enumerating + " " + game.getGameName());
                this.games.put(enumerating, game);
                enumerating += 1;
            }
        }else{
            System.out.println(resp.getMessage());
        }
    }

    private void populateGamesList(){
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        MyResponse resp = serverFacade.listGames(req);
        if(resp.getStatus() == 200){
            Vector<GameData> games = resp.getGames();
            int enumerating = 1;
            this.games.clear();
            for(GameData game: games){
                this.games.put(enumerating, game);
                enumerating += 1;
            }
        }
    }

    private void createGame() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        System.out.printf("What is your game name%n>>> ");
        req.setGameName(scanner.nextLine());
        MyResponse resp = serverFacade.createGame(req);
        if(resp.getStatus() != 200){
            System.out.println(resp.getMessage());
        }
        populateGamesList();
    }
}
