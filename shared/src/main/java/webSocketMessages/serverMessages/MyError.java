package webSocketMessages.serverMessages;

public class MyError extends ServerMessage{
    private String errorMessage;
    public MyError(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
