package webSocketMessages.serverMessages;

public class MyError extends ServerMessage{
    private String errorMessage;
    public MyError(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
