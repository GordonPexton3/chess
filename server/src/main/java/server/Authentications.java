package server;

import dataAccess.*;

import java.sql.SQLException;
import java.util.UUID;


public class Authentications {

    private static AuthDAO auth;
    private static UserDAO users;
    private static GameDAO games;

    public static void makeDAOs() throws SQLException, DataAccessException {
        auth = SQLAuthDAO.getInstance();
        users = SQLUserDAO.getInstance();
        games = SQLGameDAO.getInstance();
    }

    public static MyResponse register(MyRequest req){
        MyResponse resp = new MyResponse();
        // if the username or the password or the email is absent then it a bad request
        if(req.getUsername() == null || req.getUsername().isEmpty() ||
                req.getPassword() == null || req.getPassword().isEmpty() ||
                req.getEmail() == null || req.getEmail().isEmpty()
        ){
            resp.setMessage("Error: bad request");
            resp.setStatus(400);
        }else if(users.getUser(req.getUsername()) != null){ // if the username already produces a person, you can't
            // have a duplicate username
            resp.setMessage("Error: already taken");
            resp.setStatus(403);
        }else{ // otherwise, create the user.
            users.createUser(req.getUsername(), req.getPassword(), req.getEmail());
            String authToken = UUID.randomUUID().toString();
            new AuthorizationsDAO().createAuth(authToken, req.getUsername());
            resp.setUsername(req.getUsername());
            resp.setAuthToken(authToken);
            resp.setStatus(200);
        }
        return resp;
    }

    public static MyResponse login(MyRequest req){
        MyResponse resp = new MyResponse();
        if(req.getUsername().isEmpty()){ // if the username is absent
            resp.setMessage("Needs a username");
            resp.setStatus(400);
        }else if(req.getPassword().isEmpty()) { // if the password is absent
            resp.setMessage("Needs a password");
            resp.setStatus(400);
        }else if(users.getUser(req.getUsername()) == null){ // if the username isn't on record
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
        }else if(!users.getUser(req.getUsername()).getPassword().equals(req.getPassword())){ // if password doesn't match
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
        }else{ // otherwise log them in.
            resp.setAuthToken(new AuthorizationsDAO().createAuth(UUID.randomUUID().toString(),req.getUsername()));
            resp.setUsername(req.getUsername());
            resp.setStatus(200);
        }
        return resp;
    }

    public static MyResponse logout(MyRequest req){
        MyResponse resp = new MyResponse();
        if(authorized(req, resp)){
            auth.deleteAuth(req.getAuthToken());
            resp.setStatus(200);
        }
        return resp;
    }

    public static MyResponse clearApplication(){
        MyResponse resp = new MyResponse();
        auth.deleteAll();
        games.deleteAll();
        users.deleteAll();
        resp.setStatus(200);
        return resp;
    }

    public static boolean authorized(MyRequest req, MyResponse resp){
        try{
            auth.getUsername(req.getAuthToken());
            return true;
        }catch(DataAccessException e) {
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }
    }
}
