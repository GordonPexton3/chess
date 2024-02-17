package dataAccess;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationsDAO implements AuthDAO{
    private Map<String, String> authTokenToUsername = new HashMap<>();
    private static AuthorizationsDAO instance;

    public String getUsername(String authToken){
        return this.authTokenToUsername.get(authToken);
    }
    @Override
    public void createAuth(String username, String authToken) {
        this.authTokenToUsername.put(authToken,username);
    }

    public synchronized static AuthorizationsDAO getInstance(){
        if(instance == null) {
            instance = new AuthorizationsDAO();
        }
        return instance;
    }
}
