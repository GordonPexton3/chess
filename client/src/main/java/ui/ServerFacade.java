package ui;

import com.google.gson.Gson;
import server.MyRequest;
import server.MyResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {

    private String urlString;
    public ServerFacade(int port){
        this.urlString = "http://localhost:";
        this.urlString += String.valueOf(port);

    }
    public MyResponse register(MyRequest req) {
        return sendRequest("/user", "POST", req);
    }
    public MyResponse login(MyRequest req) { return sendRequest("/session", "POST", req); }
    public MyResponse logout(MyRequest req){ return sendRequest("/session", "DELETE", req);}
    public MyResponse listGames(MyRequest req){
        return sendRequest("/game", "GET", req);
    }
    public MyResponse createGame(MyRequest req){
        return sendRequest("/game", "POST", req);
    }
    public MyResponse joinGame(MyRequest req){
        return sendRequest("/game", "PUT", req);
    }
    private MyResponse sendRequest(String path, String method, MyRequest req) {
        try {
            URL url = new URL(urlString + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod(method);
            connection.addRequestProperty("Authorization", req.getAuthToken());
            if(!method.equals("GET")){
                connection.setDoOutput(true);
            }
            connection.connect();
            if(!method.equals("GET")){
                try (OutputStream requestBody = connection.getOutputStream()) {
                    String reqString = new Gson().toJson(req);
                    requestBody.write(reqString.getBytes());
                }
            }
            try (InputStream respBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                MyResponse resp = new Gson().fromJson(inputStreamReader, MyResponse.class);
                resp.setStatus(connection.getResponseCode());
                return resp;
            } catch (IOException e) {
                InputStream respBody = connection.getErrorStream();
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                MyResponse resp = new Gson().fromJson(inputStreamReader, MyResponse.class);
                resp.setStatus(connection.getResponseCode());
                return resp;
            }
        }catch(IOException e){
            System.out.println("You broke something in ServerFacade\n" + e);
            return new MyResponse();
        }catch(NullPointerException e){
            System.out.println("WHY DOES THIS KEEP HAPPENING ");
            return new MyResponse();
        }
    }
}
