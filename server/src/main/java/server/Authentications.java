package server;

import dataAccess.AuthorizationsDAO;
import dataAccess.UsersDAO;

import java.util.UUID;


public class Authentications {
    public static myResponse register(myRequest req){
        myResponse resp = new myResponse();
        UsersDAO users = UsersDAO.getInstance();
        if(req.getUsername() == "" || req.getUsername().isEmpty()){
            resp.setMessage("Needs a username");
            resp.setStatus(400);
        }else if(req.getPassword() == "" || req.getPassword().isEmpty()){
            resp.setMessage("Needs a password");
            resp.setStatus(400);
        }else if(req.getEmail() == null || req.getEmail().isEmpty()) {
            resp.setMessage("Needs an email");
            resp.setStatus(400);
        }else if(users.getUser(req.getUsername()) != null){
            resp.setMessage("Username already exists");
            resp.setStatus(403);
        }else{ // create the user
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
        return new myResponse();
    }

    public static myResponse logout(myRequest req){
        return new myResponse();
    }

    public static myResponse clearApplication(myRequest req){
        return new myResponse();
    }
}
