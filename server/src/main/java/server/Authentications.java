package server;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;

import java.sql.SQLException;
import java.util.UUID;


public class Authentications {

    private static SQLAuthDAO auth;
    private static SQLUserDAO users;
    private static SQLGameDAO games;


    public static void makeDAOs() throws SQLException, DataAccessException {
        auth = SQLAuthDAO.getInstance();
        users = SQLUserDAO.getInstance();
        games = SQLGameDAO.getInstance();
    }

    public static MyResponse register(MyRequest req){
        MyResponse resp = new MyResponse();
        if(goodRegisterRequest(req,resp)){
            if(usernameNotTake(req,resp)){
                try{
                    users.createUser(req.getUsername(), req.getPassword(), req.getEmail());
                    String authToken = UUID.randomUUID().toString();
                    auth.createAuth(authToken,req.getUsername());
                    resp.setAuthToken(authToken);
                    resp.setUsername(req.getUsername());
                    resp.setStatus(200);
                }catch(SQLException | DataAccessException e){
                    resp.setMessage("Problem in Authentications:register\n" + e);
                    resp.setStatus(500);
                }
            }
        }
        return resp;
    }

    private static boolean goodRegisterRequest(MyRequest req, MyResponse resp){
        // if the username or the password or the email is absent then it a bad request
        if(req.getUsername() == null || req.getUsername().isEmpty() ||
                req.getPassword() == null || req.getPassword().isEmpty() ||
                req.getEmail() == null || req.getEmail().isEmpty()
        ){
            resp.setMessage("Error: bad request");
            resp.setStatus(400);
            return false;
        }
        return true;
    }

    private static boolean usernameNotTake(MyRequest req, MyResponse resp){
        try{
            users.getUser(req.getUsername());
            resp.setMessage("Error: already taken");
            resp.setStatus(403);
            return false;
        }catch(DataAccessException e){
            return true;
        }catch(SQLException e){
            resp.setMessage("Problem in Authentications:usernameNotTaken\n" + e);
            resp.setStatus(500);
            return false;
        }
    }

    public static MyResponse login(MyRequest req){
        MyResponse resp = new MyResponse();
        if(goodRequest(req, resp)){
            if(usernameOnRecord(req,resp)){
                if(passwordMatches(req,resp)){
                    String authToken = UUID.randomUUID().toString();
                    try{
                        auth.createAuth(authToken,req.getUsername());
                        resp.setAuthToken(authToken);
                        resp.setUsername(req.getUsername());
                        resp.setStatus(200);
                    }catch(SQLException | DataAccessException e){
                        resp.setMessage("ERROR:" + e);
                        resp.setStatus(500);
                    }
                }
            }
        }
        return resp;
    }

    private static boolean passwordMatches(MyRequest req, MyResponse resp) {
        try{
            if(!users.getPassword(req.getUsername()).equals(req.getPassword())){
                throw new DataAccessException("Password does not match");
            }
            return true;
        }catch(DataAccessException e){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }catch(SQLException e){
            resp.setMessage("Problem in Authentications:passwordMatches\n" + e);
            resp.setStatus(500);
            return false;
        }
    }

    private static boolean usernameOnRecord(MyRequest req, MyResponse resp) {
        try{
            users.getUser(req.getUsername());
            return true;
        }catch(DataAccessException e){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }catch(SQLException e){
            resp.setMessage("Problem in clearApplication\n" + e);
            resp.setStatus(500);
            return false;
        }
    }

    private static boolean goodRequest(MyRequest req, MyResponse resp){
        if(req.getUsername().isEmpty()){
            resp.setMessage("Needs a username");
            resp.setStatus(400);
            return false;
        }else if(req.getPassword().isEmpty()) {
            resp.setMessage("Needs a password");
            resp.setStatus(400);
            return false;
        }
        return true;
    }

    public static MyResponse logout(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            try {
                auth.deleteAuth(req.getAuthToken());
                resp.setStatus(200);
            }catch(SQLException | DataAccessException e){
                resp.setMessage("Problem in Authentications::logout\n" + e);
                resp.setStatus(500);
            }
        }
        return resp;
    }

    public static MyResponse clearApplication(){
        MyResponse resp = new MyResponse();
        try {
            auth.deleteAll();
            games.deleteAll();
            users.deleteAll();
            resp.setStatus(200);
            return resp;
        }catch(SQLException | DataAccessException e){
            resp.setMessage("Problem in clearApplication\n" + e);
            resp.setStatus(500);
            return resp;
        }
    }

    public static boolean authorized(MyRequest req, MyResponse resp){
        try{
            auth.getUsername(req.getAuthToken());
            return true;
        }catch(DataAccessException e) {
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }catch(SQLException e){
            resp.setMessage("Error: " + e);
            resp.setStatus(500);
            return false;
        }
    }
}
