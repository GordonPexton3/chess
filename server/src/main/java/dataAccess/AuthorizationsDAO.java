package dataAccess;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationsDAO implements AuthDAO{
    private Map<String, String> authTokenToUsername = new HashMap<>();
    private static AuthorizationsDAO instance;

    public String getUsername(String authToken){
        return getInstance().authTokenToUsername.get(authToken);
    }
    @Override
    public String createAuth(String authToken, String username) {
        getInstance().authTokenToUsername.put(authToken,username);
        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) {
        getInstance().authTokenToUsername.remove(authToken);
    }

    public synchronized static AuthorizationsDAO getInstance(){
        if(instance == null) {
            instance = new AuthorizationsDAO();
        }
        return instance;
    }
}
