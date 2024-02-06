package chess;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] Board = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        Board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * the board should be accessible
     */
    public ChessPiece[][] getBoard(){
        return this.Board;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return Board[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPiece getPieceAtIndex(int r, int c) {
        return Board[r - 1][c - 1];
    }

    /**
     * @return a bool if coordinates are on the board
     */
    public boolean onBoard(int r, int c){
        if(r < 9 && r > 0 && c < 9 && c > 0){
            return true;
        }
        return false;
    }

    private final Map<Character, ChessPiece.PieceType> charToPieceType = Map.of(
            'r', ChessPiece.PieceType.ROOK,
            'n', ChessPiece.PieceType.KNIGHT,
            'b', ChessPiece.PieceType.BISHOP,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'p', ChessPiece.PieceType.PAWN
    );

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // |r|n|b|q|k|b|n|r|
        // |p|p|p|p|p|p|p|p|
        Character[] row1 = {'r','n','b','q','k','b','n','r'};
        Character[] row2 = {'p','p','p','p','p','p','p','p'};
        // for the black pieces first row
        for(int c = 0; c < 8; c ++){
            Board[7][c] = new ChessPiece(ChessGame.TeamColor.BLACK, charToPieceType.get(row1[c]));
        }
        // black pieces second row
        for(int c = 0; c < 8; c ++){
            Board[6][c] = new ChessPiece(ChessGame.TeamColor.BLACK, charToPieceType.get(row2[c]));
        }
        // white pieces first row
        for(int c = 0; c < 8; c ++){
            Board[0][c] = new ChessPiece(ChessGame.TeamColor.WHITE, charToPieceType.get(row1[c]));
        }
        // white pieces second row
        for(int c = 0; c < 8; c ++){
            Board[1][c] = new ChessPiece(ChessGame.TeamColor.WHITE, charToPieceType.get(row2[c]));
        }
    }

    private final Map<ChessPiece.PieceType, String> blackPieceTypeToChar = Map.of(
            ChessPiece.PieceType.ROOK, "R",
            ChessPiece.PieceType.KNIGHT,"N",
            ChessPiece.PieceType.BISHOP,"B",
            ChessPiece.PieceType.QUEEN,"Q",
            ChessPiece.PieceType.KING,"K" ,
            ChessPiece.PieceType.PAWN,"P"
    );
    private final Map<ChessPiece.PieceType, String> whitePieceTypeToChar = Map.of(
            ChessPiece.PieceType.ROOK, "r",
            ChessPiece.PieceType.KNIGHT,"n",
            ChessPiece.PieceType.BISHOP,"b",
            ChessPiece.PieceType.QUEEN,"q",
            ChessPiece.PieceType.KING,"k" ,
            ChessPiece.PieceType.PAWN,"p"
    );

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("\n");
        for(int r = 8; r > 0; r --){
            for(int c = 1; c < 9; c ++){
                str.append("|");
                if(getPieceAtIndex(r,c) == null){
                    str.append("_");
                }else{
                    ChessPiece piece = getPieceAtIndex(r,c);
                    if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        str.append(blackPieceTypeToChar.get(piece.getPieceType()));
                    }else{
                        str.append(whitePieceTypeToChar.get(piece.getPieceType()));
                    }
                }
            }
            str.append("|\n");
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(getBoard(), that.getBoard());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getBoard());
    }
}
