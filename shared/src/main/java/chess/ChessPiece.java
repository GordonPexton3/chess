package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType ThisPieceType;
    private final ChessGame.TeamColor ThisPieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        ThisPieceType = type;
        ThisPieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return ThisPieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return ThisPieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessPosition> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessPosition> Positions = new HashSet<>();
        ChessPiece piece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        switch (piece.getPieceType()) {
            case PieceType.KING:
                System.out.println("hey this guy is a king");
                for(int r = r0 - 1; r < r0 + 2; r ++){
                    for(int c = c0 - 1; c < c0 + 2; c ++){
                       if (r < 0 || r > 7 || r == r0 || c < 0 || c > 7 || c == c0){break;}
                       if (board.getTeamColorAt(r,c) == ThisPieceColor){break;}
                       Positions.add(new ChessPosition(r,c));
                    }
                }
            case PieceType.QUEEN:
                System.out.println("hey this guy is a Queen");
            case PieceType.BISHOP:
                System.out.println("hey this guy is a Bishop");
                //Positions.addAll(BishopsMoves(board, myPosition));
                for(int r = r0 + 1; r <= 7; r ++){
                    for(int c = c0 + 1; c <= 7; c ++){
                        if(board.getTeamColorAt(r,c) == ThisPieceColor){break;}
                    }
                }
                for(int r = r0 - 1; r >= 0; r --){
                    for(int c = c0 - 1; c >= 0; c --){

                    }
                }
                for(int r = r0 - 1; r >= 0; r --){
                    for(int c = c0 + 1; c < 7; c ++){

                    }
                }
                for(int r = r0 + 1; r < 7; r ++){
                    for(int c = c0 - 1; c >= 0; c --){

                    }
                }
            case PieceType.KNIGHT:
                System.out.println("hey this guy is a Knight");
            case PieceType.ROOK:
                System.out.println("hey this guy is a Rook");
            case PieceType.PAWN:
                System.out.println("hey this guy is a Pawn");
        }
        return Positions;
    }

    private Collection<ChessPosition> BishopsMoves(ChessBoard board, ChessPosition myPosition){
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 + 1; r < 7; r ++){
            for(int c = c0 + 1; c < 7; c ++){

            }
        }
        for(int r = r0 + 1; r < 7; r ++){
            for(int c = c0 + 1; c < 7; c ++){

            }
        }
        for(int r = r0 + 1; r < 7; r ++){
            for(int c = c0 + 1; c < 7; c ++){

            }
        }
        for(int r = r0 + 1; r < 7; r ++){
            for(int c = c0 + 1; c < 7; c ++){

            }
        }
        return Positions;
    }

}
