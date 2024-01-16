package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] Board = new ChessPiece[8][8];

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
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return Board[position.getRow()][position.getColumn()];
    }

    public ChessGame.TeamColor getTeamColorAt(int r, int c){
        if (Board[r][c] == null){
            return null;
        }
        return Board[r][c].getTeamColor();
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
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
