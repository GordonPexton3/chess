package clientTests;

import org.junit.jupiter.api.*;
import server.Authentications;
import server.MyRequest;
import server.MyResponse;
import server.Server;
import ui.ServerFacade;


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
}
