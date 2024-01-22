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
    private final ChessGame.TeamColor thisPieceColor;
    private boolean moved;
    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        ThisPieceType = type;
        thisPieceColor = pieceColor;
        if (ThisPieceType == PieceType.PAWN){
            moved = false;
        }
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
     * if this is a pawn, you can indicate that it has used its first move
     */
    public boolean movedStatus(){
        return moved;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return thisPieceColor;
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
        return ThisPieceType == that.ThisPieceType && thisPieceColor == that.thisPieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ThisPieceType, thisPieceColor);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger.
     * This wants a collection of valid moves. I am going to make that change, but
     * I keep a copy just in case.
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case PieceType.KING:
                moves = PieceMoves.kingMoves(board, myPosition, thisPieceColor);
                break;
            case PieceType.QUEEN:
                break;
            case PieceType.BISHOP:
                moves = PieceMoves.bishopMoves(board, myPosition, thisPieceColor);
                break;
            case PieceType.KNIGHT:
                moves = PieceMoves.knightMoves(board, myPosition, thisPieceColor);
                break;
            case PieceType.ROOK:

            case PieceType.PAWN:
                moves = PieceMoves.pawnMoves(board, myPosition, thisPieceColor);
        }
        return moves;
    }
}
