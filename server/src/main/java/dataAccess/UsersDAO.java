package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UsersDAO implements UserDAO{
    private Map<String, UserData> users = new HashMap<>();
    private static UsersDAO instance;

    @Override
    public UserData getUser(String username) {
        return getInstance().users.get(username);
    }

    @Override
    public String getPassword(String username) {
        for(UserData u: getInstance().users.values()){
            if(u.getUsername().equals(username)){
                return u.getPassword();
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        getInstance().users.put(username, new UserData(username, password,email));
    }

    @Override
    public void deleteAll() {
        getInstance().users.clear();
    }

    public synchronized static UsersDAO getInstance(){
        if(instance == null){
            instance = new UsersDAO();
        }
        return instance;
    }
}
