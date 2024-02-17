package server;


import com.google.gson.Gson;
import spark.Spark;

public class Server{
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

//        Spark.post("/user", new Route(){
//            @Override
//            public Object handle(spark.Request request, Response response) throws Exception {
//                System.out.println("Dude this worked!");
//                System.out.println(request.body());
//                System.out.println(request.headers("Authorization"));
//                System.out.println(request.headers());
//                return null;
//            }
//        });
//
//        Spark.post("/user", (request, response) -> register(request, response));

        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.delete("/db", this::clear);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(spark.Request req, spark.Response res) {
//        System.out.println("Dude this worked!");
//        System.out.println(req.body());
//        System.out.println(req.headers("Authorization"));
//        System.out.println("**********************");
//        System.out.println(new Gson().toJson(response));
//        System.out.println(req.headers());
//        res.status(int status Code);

        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = Authentications.register(request);
        res.status(response.getStatus());

        return new Gson().toJson(response);
    }

    private Object login(spark.Request req, spark.Response res) {
        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = Authentications.login(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    private Object logout(spark.Request req, spark.Response res) {
        myRequest request = new myRequest();
        request.setAuthToken(req.headers("Authorization"));
        System.out.println(request.getAuthToken());
        myResponse response = Authentications.logout(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    private Object clear(spark.Request req, spark.Response res) {
        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = Authentications.clearApplication(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    private Object listGames(spark.Request req, spark.Response res) {
        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = GameInteractions.listGames(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    private Object createGame(spark.Request req, spark.Response res) {
        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = GameInteractions.createGame(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    private Object joinGame(spark.Request req, spark.Response res) {
        myRequest request = new Gson().fromJson(req.body(), myRequest.class);
        myResponse response = GameInteractions.joinGame(request);
        res.status(response.getStatus());
        return new Gson().toJson(response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
