package serviceTests;

import dataAccess.AuthorizationsDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.Authentications;
import server.MyRequest;
import server.MyResponse;

public class ServiceTests {

    @AfterEach
    public void clearAuthentications(){
        AuthorizationsDAO.getInstance().deleteAll();
    }

    @Test
    public void testEmptyContent() {
        MyRequest req = new MyRequest();
        req.setUsername("Username Test");
        req.setPassword("123456");
        req.setEmail("This.is.an.email@BRO.AWESOME");
        MyResponse resp = Authentications.register(req);
        String expected = "Username Test";
        String actual = resp.getUsername();
        Assertions.assertEquals(expected, actual);
    }
}