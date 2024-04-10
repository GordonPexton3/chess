package ui;

import model.GameData;
import server.MyRequest;
import server.MyResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class UI {
    private final Map<Integer, GameData> games = new HashMap<>();
    private boolean logout;
    private String authToken;
    private ServerFacade serverFacade;

    public void run(int port){
        this.serverFacade = new ServerFacade(port);
        preLogin();
    }

    private void preLogin() {
        boolean quit = false;
        while (!quit) {
            this.logout = false;
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

    private void register() {
        MyRequest req = new MyRequest();
        Scanner scanner = new Scanner(System.in);
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

    private void joinObserver() throws Error{
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        Scanner scanner = new Scanner(System.in);
        System.out.printf("What is the number of the game you want to join as observer%n>>> ");
        GameData selectedGameData;
        try {
            int selectedGameNumber = Integer.parseInt(scanner.nextLine());
            selectedGameData = this.games.get(selectedGameNumber);
            req.setGameID(selectedGameData.getGameID());
        } catch (NumberFormatException e) {
            System.out.println("please put in a game number as appears in the game list");
            throw new Error();
        } catch (NullPointerException e){
            System.out.println("That number does not refer to an existing game.");
            throw new Error();
        }
        MyResponse resp = serverFacade.joinGame(req);
        if(resp.getStatus() == 200){
            new GamePlay(resp.getGameID(), authToken);
        }else{
            System.out.println(resp.getMessage());
        }
    }

    private void joinGame() {
        MyRequest req = new MyRequest();
        req.setAuthToken(this.authToken);
        Scanner scanner = new Scanner(System.in);
        System.out.printf("What is the number of the game you want to join as player%n>>> ");
        try {
            req.setGameID(this.games.get(Integer.valueOf(scanner.nextLine())).getGameID());
        } catch (NumberFormatException e) {
            System.out.println("please put in a game number as appears in the game list");
            throw new Error();
        } catch (NullPointerException e){
            System.out.println("That number does not refer to an existing game.");
            throw new Error();
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
//            GameData gameData = this.games.get(resp.getGameID());
            new GamePlay(resp.getGameID(), authToken);
        }else{
            System.out.println(resp.getMessage());
        }
        throw new Error();
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
        Scanner scanner = new Scanner(System.in);
        System.out.printf("What is your game name%n>>> ");
        req.setGameName(scanner.nextLine());
        MyResponse resp = serverFacade.createGame(req);
        if(resp.getStatus() == 200){
            new DrawBoard();
        }else{
            System.out.println(resp.getMessage());
        }
    }
}
