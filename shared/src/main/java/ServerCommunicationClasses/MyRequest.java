package ServerCommunicationClasses;

public class MyRequest {

    private String username;
    private String password;
    private String email;
    private String authToken;
    private Integer gameID;
    private String gameName;
    private String playerColor;
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameName(String gameName){ this.gameName = gameName; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setGameID(Integer gameID){
        this.gameID = gameID;
    }

    public void setPlayerColor(String color){
        this.playerColor = color;
    }

    public String getGameName() {
        return this.gameName;
    }

    public String getPlayerColor() {
        return this.playerColor;
    }
}
