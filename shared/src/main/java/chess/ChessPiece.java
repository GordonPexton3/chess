package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType ThisPieceType;
    private final ChessGame.TeamColor ThisPieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return ThisPieceType == that.ThisPieceType && ThisPieceColor == that.ThisPieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ThisPieceType, ThisPieceColor);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     * This wants a collection of valid moves. I am going to make that change but
     * I keep a copy just in case.
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case PieceType.KING:
                System.out.println("hey this guy is a king");
                moves = PieceMoves.KingMoves(board, myPosition, ThisPieceColor);
                break;
            case PieceType.QUEEN:
                System.out.println("hey this guy is a Queen");
            case PieceType.BISHOP:
                moves = PieceMoves.bishopMoves(board, myPosition, ThisPieceColor);
                break;
            case PieceType.KNIGHT:
                System.out.println("hey this guy is a Knight");
                break;
            case PieceType.ROOK:
                System.out.println("hey this guy is a Rook");
                break;
            case PieceType.PAWN:
                System.out.println("hey this guy is a Pawn");
                break;
        }
        return moves;
    }
}
