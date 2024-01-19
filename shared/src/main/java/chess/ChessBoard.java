package chess;

import java.util.Arrays;
import java.util.Map;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] Board = new ChessPiece[8][8];

    public static void ChessBoard() {
        
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
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return Board[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessGame.TeamColor getTeamColorAt(int r, int c){
        if (Board[r - 1][c - 1] == null){
            return null;
        }
        return Board[r - 1][c - 1].getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(Board, that.Board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(Board);
    }

    /**
     * I am using this code from TestFactory. I hope it's not considered cheating
     * They just coded it so well, I didn't know it was a technique and I would like to use it.
     */
    private Map<Character, ChessPiece.PieceType> charToTypeMap = Map.of(
            'p', ChessPiece.PieceType.PAWN,
            'n', ChessPiece.PieceType.KNIGHT,
            'r', ChessPiece.PieceType.ROOK,
            'q', ChessPiece.PieceType.QUEEN,
            'k', ChessPiece.PieceType.KING,
            'b', ChessPiece.PieceType.BISHOP);

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        /*
        |P|P|P|P|P|P|P|P|
        |R|N|B|Q|K|B|N|R|
         */
        char[] Row1PieceChars = {'r','n','b','q','k','b','n','r'};
        char[] Row2PieceChars = {'p','p','p','p','p','p','p','p'};
        for(int r = 1; r <= 2; r ++){
            for(int c = 1; c <= 8; c ++){
                if(r == 1) {
                    Board[r - 1][c - 1] = new ChessPiece(ChessGame.TeamColor.WHITE, charToTypeMap.get(Row1PieceChars[c - 1]));
                }else{
                    Board[r - 1][c - 1] = new ChessPiece(ChessGame.TeamColor.WHITE, charToTypeMap.get(Row2PieceChars[c - 1]));
                }
            }
        }
        for(int r = 8; r >= 7; r --){
            for(int c = 1; c <= 8; c ++){
                if(r == 8) {
                    Board[r - 1][c - 1] = new ChessPiece(ChessGame.TeamColor.BLACK, charToTypeMap.get(Row1PieceChars[c - 1]));
                }else{
                    Board[r - 1][c - 1] = new ChessPiece(ChessGame.TeamColor.BLACK, charToTypeMap.get(Row2PieceChars[c - 1]));
                }
            }
        }
    }

    /**
     * This allows for a easier representation of board when printed.
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int r = 7; r >= 0; r--){
            for(int c = 0; c < 8; c++){
                str.append("|");
                if(Board[r][c] == null){
                    str.append("_");
                }else{
                    ChessPiece piece = Board[r][c];
                    switch (piece.getPieceType()) {
                        case ChessPiece.PieceType.KING:
                            str.append("K");
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            str.append("Q");
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            str.append("B");
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            str.append("k");
                            break;
                        case ChessPiece.PieceType.ROOK:
                            str.append("R");
                            break;
                        case ChessPiece.PieceType.PAWN:
                            str.append("P");
                            break;
                    }
                }
            }
            str.append("|\n");
        }
        return str.toString();
    }
}
