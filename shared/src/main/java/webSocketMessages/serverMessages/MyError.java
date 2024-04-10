package webSocketMessages.serverMessages;

public class MyError extends ServerMessage{
    private String message;
    public MyError(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
}
