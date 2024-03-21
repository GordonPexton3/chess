import ui.UI;

public class Main {
    public static void main(String[] args){
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

          UI ui = new UI();
          ui.run(8080);
//        server = new Server();
//        PostServer makeAPost = new PostServer();
//        makeAPost.doPost("http://localhost:8080/user");
    }
}