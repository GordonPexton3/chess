package dataAccess;

import model.UserData;

import java.util.Vector;

public class UsersDAO implements UserDAO{
    private Vector<UserData> users = new Vector<>();
    private static UsersDAO instance;
    @Override
    public String getPassword(String username) {
        for(UserData u: users){
            if(u.getUsername() == username){
                return u.getPassword();
            }
        }
        return null;
    }
    @Override
    public void createUser(String username, String password, String email) {
        users.add(new UserData(username, password,email));
    }

    public synchronized static UsersDAO getInstance(){
        if(instance == null){
            instance = new UsersDAO();
        }
        return instance;
    }
}
