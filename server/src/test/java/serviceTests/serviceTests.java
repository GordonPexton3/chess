package serviceTests;

import dataAccess.AuthorizationsDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.Authentications;
import server.myRequest;
import server.myResponse;

public class serviceTests {

    @AfterEach
    public void clearAuthentications(){
        AuthorizationsDAO.getInstance().deleteAll();
    }

    @Test
    public void testEmptyContent() {
        myRequest req = new myRequest();
        req.setUsername("Username Test");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        myResponse resp = Authentications.register(req);
        String expected = "Username Test";
        String actual = resp.getUsername();
        Assertions.assertEquals(expected, actual);
    }
}