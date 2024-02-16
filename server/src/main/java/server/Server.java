package server;


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

//        Spark.post("/user", (request, response) -> register(request, response));

        Spark.post("/user", this::register);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(spark.Request req, spark.Response res) {
        System.out.println("DID this do anything ");
        System.out.println("Dude this worked!");
        System.out.println(req.body());
        System.out.println(req.headers("Authorization"));
        System.out.println(req.headers());
        return res;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
