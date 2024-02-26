package server;

import dataAccess.AuthorizationsDAO;
import dataAccess.GamesDAO;
import model.GameData;

import java.util.Random;

public class GameInteractions {
    public static myResponse listGames(myRequest req){
        myResponse resp = new myResponse();
        if(authorized(req, resp)){
            resp.setGames(GamesDAO.getInstance().listGames());
            resp.setStatus(200);
        }
        return resp;
    }

    public static myResponse createGame(myRequest req){
        myResponse resp = new myResponse();
        if(authorized(req, resp)){
            int newGameID = generateNewGameID();
            GamesDAO.getInstance().createGame(newGameID, req.getGameName());
            resp.setGameID(newGameID);
            resp.setStatus(200);
        }
        return resp;
    }

    private static int generateNewGameID(){
        Random rand = new Random();
        int testGameID;
        while(true){
            testGameID = rand.nextInt(9000) + 1000;
            if(GamesDAO.getInstance().getGame(testGameID) == null){
                break;
            }
        }
        return testGameID;
    }

    public static myResponse joinGame(myRequest req){
        myResponse resp = new myResponse();
        if(authorized(req, resp)){
            if(gameExists(req, resp)) {
                GameData game = GamesDAO.getInstance().getGame(req.getGameID());
                if(colorSpecified(req, resp, game)){
                    String color = req.getPlayerColor();
                    joinWithColor(req, resp, color, game);
                }
            }
        }
        return resp;
    }

    private static boolean colorSpecified(myRequest req, myResponse resp, GameData game){
        String color = req.getPlayerColor();
        if(color == null){
            resp.setMessage("You are an observer");
            resp.setStatus(200);
            return false;
        }
        if(color.equals("WHITE") || color.equals("BLACK")){
            return true;
        }
        resp.setMessage("Error: You are an observer");
        resp.setStatus(200);
        return false;
    }

    private static void joinWithColor(myRequest req, myResponse resp, String color, GameData game){
        String username = AuthorizationsDAO.getInstance().getUsername(req.getAuthToken());
        switch(color){
            case "BLACK":
                if(game.getBlackUsername() == null){
                    game.setBlackUsername(username);
                    resp.setStatus(200);
                }else{
                    resp.setMessage("Error: already taken");
                    resp.setStatus(403);
                }
                break;
            case "WHITE":
                if(game.getWhiteUsername() == null){
                    game.setWhiteUsername(username);
                    resp.setStatus(200);
                }else{
                    resp.setMessage("Error: already taken");
                    resp.setStatus(403);
                }
                break;
        }
    }

    private static boolean gameExists(myRequest req, myResponse resp){
        GameData game = GamesDAO.getInstance().getGame(req.getGameID());
        if(game == null) {
            resp.setMessage("Error: bad request");
            resp.setStatus(400);
            return false;
        }else{
            return true;
        }
    }

    private static boolean authorized(myRequest req, myResponse resp){
        if(AuthorizationsDAO.getInstance().getUsername(req.getAuthToken()) == null){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }
        return true;
    }
}
