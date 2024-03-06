package dataAccessTests;

import dataAccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Authentications;
import server.GameInteractions;
import server.MyRequest;
import server.MyResponse;

import java.sql.SQLException;
import java.util.Vector;

public class DataAccessTests {

    private String authToken;
    private Integer gameID;


    public void clearAll(){
        Authentications.clearApplication();
    }

    @BeforeEach
    public void makeDAOs(){
        try {
            Authentications.makeDAOs();
            GameInteractions.makeDAOs();
        }catch(SQLException | DataAccessException e){
            throw new RuntimeException("Database Failed to Configure \n" + e);
        }
        clearAll();
    }

    private void register(){
        MyRequest req = new MyRequest();
        req.setUsername("UsernameTest");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        MyResponse resp = Authentications.register(req);
        authToken = resp.getAuthToken();
    }


    public void makeGame(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        req.setGameName("myGameName");
        MyResponse resp = GameInteractions.createGame(req);
        gameID = resp.getGameID();
    }

    @Test
    public void registerReturnUsername() {
        MyRequest req = new MyRequest();
        req.setUsername("UsernameTest");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        MyResponse resp = Authentications.register(req);
        String expected = "UsernameTest";
        String actual = resp.getUsername();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void registerWithNoPassword(){
        MyRequest req = new MyRequest();
        req.setUsername("UsernameTest1");
        req.setPassword("1234567");
        req.setEmail("");
        MyResponse resp = Authentications.register(req);
        int actual = resp.getStatus();
        int expected = 400;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void loginReturnsAuthToken(){
        register();
        MyRequest req = new MyRequest();
        req.setUsername("UsernameTest");
        req.setPassword("123456");
        MyResponse resp = Authentications.login(req);
        authToken = resp.getAuthToken();
        Assertions.assertNotEquals(authToken, "");
    }

    @Test
    public void loginFailsWhenPasswordIncorrect(){
        MyRequest req = new MyRequest();
        req.setUsername("UsernameTest");
        req.setPassword("1");
        MyResponse resp = Authentications.login(req);
        int status = resp.getStatus();
        int expected = 401;
        Assertions.assertEquals(status, expected);
    }

    @Test
    public void logout(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = Authentications.logout(req);
        int actual = resp.getStatus();
        int expected = 200;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void logoutTwice(){
        logout();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = Authentications.logout(req);
        int actual = resp.getStatus();
        int expected = 401;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void listNoGames(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = GameInteractions.listGames(req);
        Vector<GameData> actual = resp.getGames();
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void thatIsMyGame(){
        makeAGame();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = GameInteractions.listGames(req);
        boolean foundIt = false;
        for(GameData game : resp.getGames()){
            if(game.getGameName().equals("myGameName")){
                foundIt = true;
                break;
            }
        }
        Assertions.assertTrue(foundIt);
    }

    @Test
    public void makeAGame(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        req.setGameName("myGameName");
        MyResponse resp = GameInteractions.createGame(req);
        Integer gameID = resp.getGameID();
        Assertions.assertNotNull(gameID);
        Assertions.assertEquals(resp.getStatus(), 200);
    }

    @Test
    public void makeGameWithNoName(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = GameInteractions.createGame(req);
        Integer gameID = resp.getGameID();
        Assertions.assertNull(gameID);
        Assertions.assertEquals(resp.getStatus(), 400);
    }

    @Test
    public void joinAGame(){
        makeGame();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        req.setGameID(gameID);
        req.setPlayerColor("BLACK");
        GameInteractions.joinGame(req);
        for(GameData games : GameInteractions.listGames(req).getGames()){
            if(games.getGameName().equals("myGameName")){
                Assertions.assertEquals("UsernameTest", games.getBlackUsername());
                break;
            }
        }
    }

    @Test
    public void joinAGameThatDoesNotExist(){
        makeGame();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        req.setGameID(1);
        req.setPlayerColor("BLACK");
        MyResponse resp = GameInteractions.joinGame(req);
        Assertions.assertEquals(resp.getStatus(),400);
    }

    @Test
    public void clearAuthToken(){
        register();
        makeGame();
        MyResponse resp = Authentications.clearApplication();
        Assertions.assertNull(resp.getAuthToken());
    }

    @Test
    public void clearGameList(){
        makeGame();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = GameInteractions.listGames(req);
        Assertions.assertFalse(resp.getGames().isEmpty());
        Authentications.clearApplication();
        register();
        req.setAuthToken(authToken);
        resp = GameInteractions.listGames(req);
        Assertions.assertTrue(resp.getGames().isEmpty());
    }

    @Test
    public void incorrectPasswordFormat(){
        MyRequest req = new MyRequest();
        req.setUsername("DROP TABLE games;");
        req.setPassword("DROP TABLE users");
        req.setEmail("DROP TABLE auth");
        MyResponse resp = Authentications.register(req);
        String expected = "Problem in Authentications:usernameNotTaken\n" +
                "java.sql.SQLException: Username isn't acceptable";
        String actual = resp.getMessage();
        Assertions.assertEquals(expected, actual);
    }
}