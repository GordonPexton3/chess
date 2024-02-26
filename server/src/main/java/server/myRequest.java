package server;

public class myRequest {

    private String username;
    private String password;
    private String email;
    private String authToken;
    private String gameName;
    private String playerColor;
    private int gameID;
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

    public String getGameName() {
        return gameName;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

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

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }
}
