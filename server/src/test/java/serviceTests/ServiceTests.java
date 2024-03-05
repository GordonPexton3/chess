package serviceTests;

import dataAccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Authentications;
import server.GameInteractions;
import server.MyRequest;
import server.MyResponse;

import java.sql.SQLException;
import java.util.Vector;

public class ServiceTests {

    private String authToken;
    private Integer gameID;

    @AfterEach
    public void clearAll(){
        Authentications.clearApplication();
    }

    @BeforeAll
    public static void makeDAOs(){
        try {
            Authentications.makeDAOs();
            GameInteractions.makeDAOs();
        }catch(SQLException | DataAccessException e){
            throw new RuntimeException("Database Failed to Configure \n" + e);
        }
    }

    private void register(){
        MyRequest req = new MyRequest();
        req.setUsername("Username Test");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        MyResponse resp = Authentications.register(req);
        authToken = resp.getAuthToken();
    }

    private void makeGame(){
        register();
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        req.setGameName("DUDE THIS IS MY GAME");
        MyResponse resp = GameInteractions.createGame(req);
        gameID = resp.getGameID();
    }

    @Test
    public void registerReturnUsername() {
        MyRequest req = new MyRequest();
        req.setUsername("Username Test");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        MyResponse resp = Authentications.register(req);
        String expected = "Username Test";
        String actual = resp.getUsername();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void registerWithNoPassword(){
        MyRequest req = new MyRequest();
        req.setUsername("Username Test1");
        req.setPassword("1234567");
        req.setEmail("");
        MyResponse resp = Authentications.register(req);
        int actual = resp.getStatus();
        int expected = 400;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void loginReturnsAuthToken(){
        MyRequest req = new MyRequest();
        req.setUsername("Username Test");
        req.setPassword("123456");
        MyResponse resp = Authentications.login(req);
        authToken = resp.getAuthToken();
        Assertions.assertNotEquals(authToken, "");
    }

    @Test
    public void loginFailsWhenPasswordIncorrect(){
        MyRequest req = new MyRequest();
        req.setUsername("Username Test");
        req.setPassword("1");
        MyResponse resp = Authentications.login(req);
        int status = resp.getStatus();
        int expected = 401;
        Assertions.assertEquals(status, expected);
    }

    @Test
    public void logout(){
        MyRequest req = new MyRequest();
        req.setAuthToken(authToken);
        MyResponse resp = Authentications.logout(req);
        int actual = resp.getStatus();
        int expected = 401;
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void logoutTwice(){
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
            if(game.getGameName().equals("DUDE THIS IS MY GAME")){
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
        req.setGameName("DUDE THIS IS MY GAME");
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
            if(games.getGameName().equals("DUDE THIS IS MY GAME")){
                Assertions.assertEquals(games.getBlackUsername(), "Username Test");
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
    public void toDatabaseAndBack(){
        register();
    }
}