package server;

import dataAccess.AuthorizationsDAO;
import dataAccess.DataAccessException;
import dataAccess.GamesDAO;
import model.GameData;

import java.util.Random;

public class GameInteractions {
    public static MyResponse listGames(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            resp.setGames(GamesDAO.getInstance().listGames());
            resp.setStatus(200);
        }
        return resp;
    }

    public static MyResponse createGame(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            if(gameNameNotNull(req, resp)){
                Integer newGameID = generateNewGameID();
                GamesDAO.getInstance().createGame(newGameID, req.getGameName());
                resp.setGameID(newGameID);
                resp.setStatus(200);
            }
        }
        return resp;
    }

    private static boolean gameNameNotNull(MyRequest req, MyResponse resp){
        if(req.getGameName() != null){
            return true;
        }
        resp.setMessage("Error: must give game a name");
        resp.setStatus(400);
        return false;
    }

    private static int generateNewGameID(){
        Random rand = new Random();
        int testGameID;
        while(true){
            testGameID = rand.nextInt(9000) + 1000;
            try {
                GamesDAO.getInstance().getGame(testGameID);
            }catch(DataAccessException e){
                break;
            }
        }
        return testGameID;
    }

    public static MyResponse joinGame(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            try{
                GameData game = GamesDAO.getInstance().getGame(req.getGameID());
                if(colorSpecified(req, resp)){
                    String color = req.getPlayerColor();
                    joinWithColor(req, resp, color, game);
                }
            }catch(DataAccessException e){
                resp.setMessage("Error: bad request");
                resp.setStatus(400);
            }
        }
        return resp;
    }

    private static boolean colorSpecified(MyRequest req, MyResponse resp){
        String color = req.getPlayerColor();
        if(color == null || !(color.equals("WHITE") || color.equals("BLACK"))){
            resp.setMessage("You are an observer");
            resp.setStatus(200);
            return false;
        }
            return true;
    }

    private static void joinWithColor(MyRequest req, MyResponse resp, String color, GameData game) {
        try {
            String username = AuthorizationsDAO.getInstance().getUsername(req.getAuthToken());
            switch (color) {
                case "BLACK":
                    if (game.getBlackUsername() == null) {
                        game.setBlackUsername(username);
                        resp.setStatus(200);
                    } else {
                        resp.setMessage("Error: already taken");
                        resp.setStatus(403);
                    }
                    break;
                case "WHITE":
                    if (game.getWhiteUsername() == null) {
                        game.setWhiteUsername(username);
                        resp.setStatus(200);
                    } else {
                        resp.setMessage("Error: already taken");
                        resp.setStatus(403);
                    }
                    break;
            }
        } catch (DataAccessException e) {
            resp.setMessage("ERROR: username not found");
            resp.setStatus(500);
        }
    }

    private static boolean authorized(MyRequest req, MyResponse resp){
        return Authentications.authorized(req, resp);
    }
}
