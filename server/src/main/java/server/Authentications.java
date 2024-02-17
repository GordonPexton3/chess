package server;

import dataAccess.AuthorizationsDAO;


public class Authentications {
    public static myResponse register(myRequest req){
        myResponse resp = new myResponse();
        resp.setAuthToken(req.getAuthToken());
        resp.setUsername(req.getUsername());
        resp.setMessage("HEY WE LANDED IN REGISTER");
        AuthorizationsDAO auths = AuthorizationsDAO.getInstance();
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
