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
    public ChessPosition Position;
    private PieceType ThisPieceType;
    private ChessGame.TeamColor ThisPieceColor;
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
        switch (piece.getPieceType()) {
            case PieceType.KING:
                System.out.println("hey this guy is a king");
            case PieceType.QUEEN:
                System.out.println("hey this guy is a Queen");
            case PieceType.BISHOP:
                System.out.println("hey this guy is a Bishop");
                return Positions;
            case PieceType.KNIGHT:
                System.out.println("hey this guy is a Knight");
            case PieceType.ROOK:
                System.out.println("hey this guy is a Rook");
            case PieceType.PAWN:
                System.out.println("hey this guy is a Pawn");
        }
        return Positions;
    }
}
