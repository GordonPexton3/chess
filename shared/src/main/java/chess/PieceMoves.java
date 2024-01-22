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
    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor){
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        ChessPosition[] positions = {new ChessPosition(2,-1), new ChessPosition(2,1),
            new ChessPosition(1,2), new ChessPosition(-1,2),
            new ChessPosition(-2,1), new ChessPosition(-2,-1),
            new ChessPosition(-1,-2), new ChessPosition(1,-2)};
        for(int i = 0; i < positions.length; i ++){
            int r = r0 + positions[i].getRow();
            int c = c0 + positions[i].getColumn();
            // if the numbers are out of bounds go to the next number
            if(!(r < 9 && r > 0 && c < 9 && c > 0)){continue;}
            // if it's our piece, can't go there.
            if(board.getTeamColorAt(r,c) == thisPieceColor){continue;}
            // if its anything else which included nobody being there or it being the other team, you can go there.
            moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
        }
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
