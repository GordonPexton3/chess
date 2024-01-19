package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Contains all the methods that return a collection of possible movements
 * for each piece.
 *
 */
public class PieceMoves {

    public PieceMoves(){

    }

    /**
     * Return a collection of the possible moves for a
     * Knight
     */
    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor ThisPieceColor){
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        return moves;
    }

    /**
     * Return a collection of the possible moves for a king
     */
    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor){
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 - 1; r <= r0 + 1; r ++){
            for(int c = c0 - 1; c <= c0 + 1; c ++){
                if (!(r <= 0 || r > 8 || c <= 0 || c > 8 || (r == r0 && c == c0)) &&
                        (board.getTeamColorAt(r,c) != thisPieceColor)){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
            }
        }
        return moves;
    }

    /**
     * Returns a collection of the possible moves for bishop
     */
    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor){
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 + 1, c = c0 + 1; r <=8  && c <= 8; r ++, c ++){
            if(board.getTeamColorAt(r,c) == thisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));               break;
            }
        }
        for(int r = r0 - 1, c = c0 + 1; r > 0 && c <= 8; r --, c ++){
            if(board.getTeamColorAt(r,c) == thisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                break;
            }
        }
        for(int r = r0 - 1, c = c0 - 1; r > 0 && c > 0; r --, c --){
            if(board.getTeamColorAt(r,c) == thisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                break;
            }
        }
        for(int r = r0 + 1, c = c0 - 1; r <= 8 && c > 0; r ++, c --) {
            if (board.getTeamColorAt(r, c) == thisPieceColor) {
                break;
            }
            if (board.getTeamColorAt(r, c) == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                break;
            }
        }

        return moves;
    }
}
