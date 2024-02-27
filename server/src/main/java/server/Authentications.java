package server;

import dataAccess.AuthorizationsDAO;
import dataAccess.DataAccessException;
import dataAccess.GamesDAO;
import dataAccess.UsersDAO;

import java.util.UUID;


public class Authentications {
    public static myResponse register(myRequest req){
        myResponse resp = new myResponse();
        UsersDAO users = UsersDAO.getInstance();
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

    public static myResponse login(myRequest req){
        myResponse resp = new myResponse();
        UsersDAO users = UsersDAO.getInstance();
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

    public static myResponse logout(myRequest req){
        myResponse resp = new myResponse();
        if(userAuthorized(req, resp)){
            AuthorizationsDAO.getInstance().deleteAuth(req.getAuthToken());
            resp.setStatus(200);
        }
        return resp;
    }

    public static myResponse clearApplication(){
        myResponse resp = new myResponse();
        AuthorizationsDAO.getInstance().deleteAll();
        GamesDAO.getInstance().deleteAll();
        UsersDAO.getInstance().deleteAll();
        resp.setStatus(200);
        return resp;
    }

    private static boolean userAuthorized(myRequest req, myResponse resp){
        try{
            AuthorizationsDAO.getInstance().getUsername(req.getAuthToken());
            return true;
        }catch(DataAccessException e) {
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }
    }
}
