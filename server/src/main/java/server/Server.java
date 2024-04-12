package server;


import ServerCommunicationClasses.MyRequest;
import ServerCommunicationClasses.MyResponse;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;

import java.sql.SQLException;

@WebSocket
public class Server{
    private final WSServer wSserver;
    public Server(){
        this.wSserver = new WSServer();
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", wSserver);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.delete("/db", this::clear);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();

        try {
            Authentications.makeDAOs();
            GameInteractions.makeDAOs();
        }catch(SQLException | DataAccessException e){
            throw new RuntimeException("Database Failed to Configure \n" + e);
        }

        return Spark.port();
    }

    private Object register(spark.Request req, spark.Response res) {
        MyRequest request = new Gson().fromJson(req.body(), MyRequest.class);
        MyResponse response = Authentications.register(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object login(spark.Request req, spark.Response res) {
        MyRequest request = new Gson().fromJson(req.body(), MyRequest.class);
        MyResponse response = Authentications.login(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object logout(spark.Request req, spark.Response res) {
        MyRequest request = new MyRequest();
        request.setAuthToken(req.headers("Authorization"));
        MyResponse response = Authentications.logout(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object clear(spark.Request req, spark.Response res) {
        MyResponse response = Authentications.clearApplication();
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object listGames(spark.Request req, spark.Response res) {
        MyRequest request = new MyRequest();
        request.setAuthToken(req.headers("Authorization"));
        MyResponse response = GameInteractions.listGames(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object createGame(spark.Request req, spark.Response res) {
        MyRequest request = new Gson().fromJson(req.body(), MyRequest.class);
        request.setAuthToken(req.headers("Authorization"));
        MyResponse response = GameInteractions.createGame(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private Object joinGame(spark.Request req, spark.Response res) {
        MyRequest request = new Gson().fromJson(req.body(), MyRequest.class);
        request.setAuthToken(req.headers("Authorization"));
        MyResponse response = GameInteractions.joinGame(request);
        setStatus(res, response);
        return new Gson().toJson(response);
    }

    private void setStatus(spark.Response res, MyResponse myRes){
        res.status(myRes.getStatus());
        myRes.setStatus(null);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
