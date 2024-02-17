package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UsersDAO implements UserDAO{
    private Map<String, UserData> users = new HashMap<>();
    private static UsersDAO instance;

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public String getPassword(String username) {
        for(UserData u: users.values()){
            if(u.getUsername() == username){
                return u.getPassword();
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        users.put(username, new UserData(username, password,email));
    }

    public synchronized static UsersDAO getInstance(){
        if(instance == null){
            instance = new UsersDAO();
        }
        return instance;
    }
}
