package server;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.Random;

public class GameInteractions {

    private static SQLAuthDAO auth;
    private static SQLGameDAO games;

//    static {
//        try {
//            auth = SQLAuthDAO.getInstance();
//            games = SQLGameDAO.getInstance();
//        }catch(SQLException | DataAccessException e){
//            System.out.println("We got some problems here");
//        }
//    }

    public static void makeDAOs() throws SQLException, DataAccessException {
        auth = SQLAuthDAO.getInstance();
        games = SQLGameDAO.getInstance();
    }

    public static MyResponse listGames(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            try {
                resp.setGames(games.listGames());
                resp.setStatus(200);
            }catch(SQLException | DataAccessException e){
                resp.setMessage("Problem in GameInteractions::listGames\n"+e);
                resp.setStatus(500);
            }
        }
        return resp;
    }

    public static MyResponse createGame(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            if(gameNameNotNull(req, resp)){
                try {
                    Integer newGameID = generateNewGameID(resp);
                    games.createGame(newGameID, req.getGameName());
                    resp.setGameID(newGameID);
                    resp.setStatus(200);
                }catch(SQLException | DataAccessException e){
                    resp.setMessage("Problem in GameInteractions::createGame\n"+e);
                    resp.setStatus(500);
                }
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

    private static int generateNewGameID(MyResponse resp){
        Random rand = new Random();
        int testGameID;
        while(true){
            testGameID = rand.nextInt(9000) + 1000;
            try {
                games.getGame(testGameID);
            }catch(DataAccessException e){
                break;
            }catch(SQLException e){
                resp.setMessage("Problem in GameInteractions::generateNewGameID\n"+e);
                resp.setStatus(500);
            }
        }
        return testGameID;
    }

    public static MyResponse joinGame(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            try{
                GameData game = games.getGame(req.getGameID());
                if(colorSpecified(req, resp)){
                    String color = req.getPlayerColor();
                    joinWithColor(req, resp, color, game);
                }
            }catch(DataAccessException e){
                resp.setMessage("Error: bad request");
                resp.setStatus(400);
            }catch(SQLException e){
                resp.setMessage("Problem in GameInteractions::joinGame\n"+e);
                resp.setStatus(500);
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
            String username = auth.getUsername(req.getAuthToken());
            switch (color) {
                case "BLACK":
                    if (game.getBlackUsername() == null) {
                        game.setBlackUsername(username);
                        games.updateGame(req.getGameID(), game);
                        resp.setStatus(200);
                    } else {
                        resp.setMessage("Error: already taken");
                        resp.setStatus(403);
                    }
                    break;
                case "WHITE":
                    if (game.getWhiteUsername() == null) {
                        game.setWhiteUsername(username);
                        games.updateGame(req.getGameID(), game);
                        resp.setStatus(200);
                    } else {
                        resp.setMessage("Error: already taken");
                        resp.setStatus(403);
                    }
                    break;
            }
        } catch (DataAccessException | SQLException e) {
            resp.setMessage("Problem in GameInteractions::joinWithColor"+e);
            resp.setStatus(500);
        }
    }

    private static boolean authorized(MyRequest req, MyResponse resp){
        return Authentications.authorized(req, resp);
    }
}
