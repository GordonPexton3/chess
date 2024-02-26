package server;

import dataAccess.AuthorizationsDAO;
import dataAccess.GamesDAO;
import dataAccess.UsersDAO;

import java.util.UUID;


public class Authentications {
    public static myResponse register(myRequest req){
        myResponse resp = new myResponse();
        UsersDAO users = UsersDAO.getInstance();
        if(req.getUsername() == null || req.getUsername().isEmpty() ||
                req.getPassword() == null || req.getPassword().isEmpty() ||
                req.getEmail() == null || req.getEmail().isEmpty()
        ){
            resp.setMessage("Error: bad request");
            resp.setStatus(400);
        }else if(users.getUser(req.getUsername()) != null){
            resp.setMessage("Error: already taken");
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
        myResponse resp = new myResponse();
        UsersDAO users = UsersDAO.getInstance();
        if(req.getUsername() == "" || req.getUsername().isEmpty()){
            resp.setMessage("Needs a username");
            resp.setStatus(400);
        }else if(req.getPassword() == "" || req.getPassword().isEmpty()) {
            resp.setMessage("Needs a password");
            resp.setStatus(400);
        }else if(users.getUser(req.getUsername()) == null){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
        }else if(!users.getUser(req.getUsername()).getPassword().equals(req.getPassword())){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
        }else{
            resp.setAuthToken(new AuthorizationsDAO().createAuth(UUID.randomUUID().toString(),req.getUsername()));
            resp.setUsername(req.getUsername());
            resp.setStatus(200);
        }
        return resp;
    }

    public static myResponse logout(myRequest req){
        myResponse resp = new myResponse();
        AuthorizationsDAO authorizations = new AuthorizationsDAO();
        if(Authorized(req, resp)){
            authorizations.deleteAuth(req.getAuthToken());
            resp.setStatus(200);
        }
        return resp;
    }

    public static myResponse clearApplication(myRequest req){
        myResponse resp = new myResponse();
        AuthorizationsDAO.getInstance().deleteAll();
        GamesDAO.getInstance().deleteAll();
        UsersDAO.getInstance().deleteAll();
        resp.setStatus(200);
        return resp;
    }

    private static boolean Authorized(myRequest req, myResponse resp){
        if(AuthorizationsDAO.getInstance().getUsername(req.getAuthToken()) == null){
            resp.setMessage("Error: unauthorized");
            resp.setStatus(401);
            return false;
        }
        return true;
    }
}
