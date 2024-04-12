package clientTests;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Authentications;
import model.MyRequest;
import model.MyResponse;
import server.Server;
import ui.ServerFacade;

import java.util.Vector;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade SF;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        SF = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearApplication(){
        Authentications.clearApplication();
    }

    // I need 14 tests

    @Test
    public void register(){
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        MyResponse resp = SF.register(req);
        if(resp.getStatus() == 200){
            Assertions.assertTrue(true);
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void registerFailure(){
        // Testing to see if you can register with the same username
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        SF.register(req);
        MyResponse resp = SF.register(req);
        if(resp.getStatus() == 200){
            Assertions.fail();
        }else{
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void login(){
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        SF.register(req);
        MyResponse resp = SF.login(req);
        if(resp.getStatus() == 200){
            Assertions.assertTrue(true);
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void loginFailure(){
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        SF.register(req);
        req.setPassword("IncorrectPasswordTest");
        MyResponse resp = SF.login(req);
        if(resp.getStatus() == 200){
            Assertions.fail();
        }else{
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void logout(){
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        MyResponse resp = SF.register(req);
        req.setAuthToken(resp.getAuthToken());
        resp = SF.logout(req);
        if(resp.getStatus() == 200){
            Assertions.assertTrue(true);
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void logoutFailure(){
        MyRequest req = new MyRequest();
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        SF.register(req);
        req.setAuthToken("IncorrectAuthToken");
        MyResponse resp = SF.logout(req);
        if(resp.getStatus() == 200){
            Assertions.fail();
        }else{
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createGame(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName("GameNameTest");
        MyResponse resp = SF.createGame(req);
        if(resp.getStatus() == 200 && resp.getGameID() != null){
            Assertions.assertTrue(true);
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void createGameFail(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName(null);
        MyResponse resp = SF.createGame(req);
        if(resp.getStatus() == 200 && resp.getGameID() != null){
            Assertions.fail();
        }else{
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void listGames(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName("GameNameTest");
        SF.createGame(req);
        MyResponse resp = SF.listGames(req);
        if(resp.getStatus() == 200) {
            Assertions.assertFalse(resp.getGames().isEmpty());
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void joinGameObserver(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName("GameNameTest");
        MyResponse resp = SF.createGame(req);
        req.setGameID(resp.getGameID());
        resp = SF.joinGame(req);
        if(resp.getStatus() == 200) {
            Assertions.assertEquals("You are an observer", resp.getMessage());
        }else{
            Assertions.fail();
        }
    }

    @Test
    public void joinGameObserverFail(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName("GameNameTest");
        SF.createGame(req);
        req.setGameID(0);
        MyResponse resp = SF.joinGame(req);
        if(resp.getStatus() == 200) {
            Assertions.fail();
        }else{
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void joinGameAsBlack(){
        MyRequest req = new MyRequest();
        registerAUser(req);
        req.setGameName("GameNameTest");
        MyResponse resp = SF.createGame(req);
        req.setGameID(resp.getGameID());
        req.setPlayerColor("BLACK");
        SF.joinGame(req);
        resp = SF.listGames(req);
        if(resp.getStatus() == 200) {
            Vector<GameData> games = resp.getGames();
            Assertions.assertEquals("UserTest", games.getFirst().getBlackUsername());
        }else{
            Assertions.fail();
        }
    }

    private void registerAUser(MyRequest req){
        req.setUsername("UserTest");
        req.setPassword("PasswordTest");
        req.setEmail("EmailTest");
        req.setAuthToken(SF.register(req).getAuthToken());
    }

}
