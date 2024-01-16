package chess;

import javax.print.attribute.standard.MediaSize;
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
        System.out.print(board);
        switch (piece.getPieceType()) {
            case PieceType.KING:
                System.out.println("hey this guy is a king");
                Positions = PieceMoves.KingMoves(board, myPosition, ThisPieceColor);
                break;
            case PieceType.QUEEN:
                System.out.println("hey this guy is a Queen");
            case PieceType.BISHOP:
                System.out.println("hey this guy is a Bishop at " + r0 + ", " + c0);
                Positions = PieceMoves.BishopMoves(board, myPosition, ThisPieceColor);
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
        return Positions;
    }
}
