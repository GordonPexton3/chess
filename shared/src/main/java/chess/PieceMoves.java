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
     * This is the method that returns the moves that the queen wil make
     */
    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor) {
        Collection<ChessMove> moves = new HashSet<>();
        moves.addAll(rookMoves(board, myPosition, thisPieceColor));
        moves.addAll(bishopMoves(board, myPosition, thisPieceColor));
        return moves;
    }

    /**
     * This method returns teh moves that a rook can make
     */
    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor) {
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 + 1; r < 9; r++){
            if(board.getTeamColorAt(r,c0) == thisPieceColor){break;}
            if(board.getTeamColorAt(r,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c0), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c0), null));
                break;
            }
        }
        for(int r = r0 - 1; r > 0; r--){
            if(board.getTeamColorAt(r,c0) == thisPieceColor){break;}
            if(board.getTeamColorAt(r,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c0), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r, c0), null));
                break;
            }
        }
        for(int c = c0 + 1; c < 9; c++){
            if(board.getTeamColorAt(r0,c) == thisPieceColor){break;}
            if(board.getTeamColorAt(r0,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0, c), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r0, c), null));
                break;
            }
        }
        for(int c = c0 - 1; c > 0; c--){
            if(board.getTeamColorAt(r0,c) == thisPieceColor){break;}
            if(board.getTeamColorAt(r0,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0, c), null));
            }else{
                moves.add(new ChessMove(myPosition, new ChessPosition(r0, c), null));
                break;
            }
        }
        return moves;
    }

    /**
     * This is the method that returns the moves that a pawn can dd
     */
    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor thisPieceColor){
        Collection<ChessMove> moves = new HashSet<>();
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            if(r0 == 7 && board.getTeamColorAt(r0-1,c0) == null &&
                    board.getTeamColorAt(r0-2,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0-2, c0), null));
            }
        }else{
            if(r0 == 2 && board.getTeamColorAt(r0+1,c0) == null &&
                    board.getTeamColorAt(r0+2,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0+2, c0), null));
            }
        }
        ChessPosition[] positions = new ChessPosition[3];
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            positions[0] = new ChessPosition(-1,-1);
            positions[1] = new ChessPosition(-1,0);
            positions[2] = new ChessPosition(-1,1);
        }else{
            positions[0] = new ChessPosition(1,-1);
            positions[1] = new ChessPosition(1,0);
            positions[2] = new ChessPosition(1,1);
        }
        for(ChessPosition pos : positions){
            int r = r0 + pos.getRow();
            int c = c0 + pos.getColumn();
            // if the numbers are out of bounds go to the next number
            if(!(r < 9 && r > 0 && c < 9 && c > 0)){continue;}
            // if it's our piece, can't go there.
            if(board.getTeamColorAt(r,c) == thisPieceColor){continue;}
            // if we are going foreword but there is a piece there, can't do it.
            if(c == c0 && board.getTeamColorAt(r,c) != null){continue;}
            // if we are going foreword and there is nothing there, that is possible
            if(c == c0 && board.getTeamColorAt(r,c) == null){
                if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && r == 1){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.ROOK));
                }else if(piece.getTeamColor() == ChessGame.TeamColor.WHITE && r == 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.ROOK));
                }else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
            }
            // if we are not going foreword, but diagonal, and there is a piece there, it must
            // be an opposing piece and we can go there.
            if(c != c0 && board.getTeamColorAt(r,c) != null){
                // if the piece reached the other side
                if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && r == 1){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.ROOK));
                }else if(piece.getTeamColor() == ChessGame.TeamColor.WHITE && r == 8){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), ChessPiece.PieceType.ROOK));
                }else{
                    moves.add(new ChessMove(myPosition, new ChessPosition(r, c), null));
                }
            }
        }
        return moves;
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
        for(ChessPosition pos : positions){
            int r = r0 + pos.getRow();
            int c = c0 + pos.getColumn();
            // if the numbers are out of bounds go to the next number
            if(!(r < 9 && r > 0 && c < 9 && c > 0)){continue;}
            // if it's our piece, can't go there.
            if(board.getTeamColorAt(r,c) == thisPieceColor){continue;}
            // if it's anything else which included nobody being there or it being the other team, you can go there.
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
