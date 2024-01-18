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
     * Return a collection of the possible moves for a king
     */
    public static Collection<ChessPosition> KingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor ThisPieceColor){
        Collection<ChessPosition> Positions = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 - 1; r < r0 + 2; r ++){
            for(int c = c0 - 1; c < c0 + 2; c ++){
                if (r < 0 || r > 7 || r == r0 || c < 0 || c > 7 || c == c0){break;}
                if (board.getTeamColorAt(r,c) == ThisPieceColor){break;}
                Positions.add(new ChessPosition(r,c));
            }
        }
        return Positions;
    }

    /**
     * Returns a collection of the possible moves for bishop
     */
    public static Collection<ChessPosition> BishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor ThisPieceColor){
        Collection<ChessPosition> Positions = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 + 1, c = c0 + 1; r <=8  && c <= 8; r ++, c ++){
            if(board.getTeamColorAt(r,c) == ThisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                Positions.add(new ChessPosition(r,c));
            }else{
                Positions.add(new ChessPosition(r,c));
                break;
            }
        }
        for(int r = r0 - 1, c = c0 - 1; r > 0 && c > 0; r --, c --){
            if(board.getTeamColorAt(r,c) == ThisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                Positions.add(new ChessPosition(r,c));
            }else{
                Positions.add(new ChessPosition(r,c));
                break;
            }
        }
        for(int r = r0 + 1, c = c0 - 1; r <= 8 && c > 0; r ++, c --){
            if(board.getTeamColorAt(r,c) == ThisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                Positions.add(new ChessPosition(r,c));
            }else{
                Positions.add(new ChessPosition(r,c));
                break;
            }
        }
        for(int r = r0 - 1, c = c0 + 1; r > 0 && c <= 8; r --, c ++){
            if(board.getTeamColorAt(r,c) == ThisPieceColor){break;}
            if(board.getTeamColorAt(r,c) == null){
                Positions.add(new ChessPosition(r,c));
            }else{
                Positions.add(new ChessPosition(r,c));
                break;
            }
        }
        return Positions;
    }
}
