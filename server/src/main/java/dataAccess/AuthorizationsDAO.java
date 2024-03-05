package dataAccess;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationsDAO implements AuthDAO{
    private Map<String, String> authTokenToUsername = new HashMap<>();
    private static AuthorizationsDAO instance;

    public String getUsername(String authToken) throws DataAccessException {
        String username = getInstance().authTokenToUsername.get(authToken);
        if(username == null){
            throw new DataAccessException("Username does not exist");
        }else{
            return username;
        }
    }

    @Override
    public void createAuth(String authToken, String username) {
        getInstance().authTokenToUsername.put(authToken,username);
    }

    @Override
    public void deleteAuth(String authToken) {
        getInstance().authTokenToUsername.remove(authToken);
    }

    @Override
    public void deleteAll() {
        getInstance().authTokenToUsername.clear();
    }

    public synchronized static AuthorizationsDAO getInstance(){
        if(instance == null) {
            instance = new AuthorizationsDAO();
        }
        return instance;
    }
}
