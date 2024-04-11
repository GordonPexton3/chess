package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;
    public boolean gameOver = false;
    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> piecesMoves(ChessPosition startPosition){
        ChessPiece piece = getChessBoard().getPiece(startPosition);
        return piece.pieceMoves(getChessBoard(),startPosition);
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getChessBoard().getPiece(startPosition);
        ChessBoard board = getChessBoard();
        // if there is no piece there, return a list with no moves in it
        if(piece == null){
            return null;
        }else{ // if there is a piece there, then call its pieceMoves and return that list of moves
            Collection<ChessMove> moves = piece.pieceMoves(getChessBoard(),startPosition);
            Collection<ChessMove> listOfValidMoves = new HashSet<>();
            for(ChessMove move : moves){
                // make the move
                ChessPiece tempPiece = board.getPiece(move.getEndPosition());
                board.addPiece(move.getEndPosition(),piece);
                board.addPiece(move.getStartPosition(), null);
                // if the move doesn't put the board in check, add it
                if(!isInCheck(piece.getTeamColor())){
                    listOfValidMoves.add(move);
                }
                // undo the move
                board.addPiece(move.getStartPosition(),piece);
                board.addPiece(move.getEndPosition(), tempPiece);
            }
            return listOfValidMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(gameOver){
            throw new InvalidMoveException("The game is over you dork - Alejandro");
        }
        // TRACE
        ChessBoard board = getChessBoard();
        ChessPiece piece = getChessBoard().getPiece(move.getStartPosition());
        // not teams turn to play
        if(getTeamTurn() != piece.getTeamColor()){
            throw new InvalidMoveException();
        }
        // if move is not contained in valid moves
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(!validMoves.contains(move)){
            throw new InvalidMoveException();
        }
        // if the move is valid and the piece is there...
        // if the board is in check
        if(isInCheck(getTeamTurn())){
            // perform the move
            ChessPiece tempPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(),piece);
            board.addPiece(move.getStartPosition(), null);
            if(!isInCheck(getTeamTurn())){ // the move got the team out of check
                if(getTeamTurn() == TeamColor.BLACK){
                    setTeamTurn(TeamColor.WHITE);
                }else{
                    setTeamTurn(TeamColor.BLACK);
                }
            }else{ // naw your still in check
                // undo the move
                getChessBoard().addPiece(move.getStartPosition(),piece);
                getChessBoard().addPiece(move.getEndPosition(), tempPiece);
                throw new InvalidMoveException();
            }
        }else{ // you are not in check
            getChessBoard().addPiece(move.getEndPosition(),piece); // add it in the end position of the move
            getChessBoard().addPiece(move.getStartPosition(), null); // remove it from the starting position of move
            // update the turn
            if(getTeamTurn() == TeamColor.BLACK){
                setTeamTurn(TeamColor.WHITE);
            }else{
                setTeamTurn(TeamColor.BLACK);
            }
        }
        promotePawns(move, piece.getTeamColor());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     * isInCheck: Returns true if the specified team’s King could be captured by an opposing piece
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        // iterate through the board ...
        int r = 1;
        ChessBoard board = getChessBoard();
        for(ChessPiece[] row: board.getBoard()) {
            int c = 1;
            for (ChessPiece piece : row) {
                if (piece != null) { // if there is a piece there ...
                    if (piece.getTeamColor() != teamColor) { // and it is not on this team ...
                        for(ChessMove opponentMove: piecesMoves(new ChessPosition(r,c))){ // for each of its valid moves ...
                            if(opponentMove.getEndPosition().getRow() == kingPosition.getRow()) {
                                if(opponentMove.getEndPosition().getColumn() == kingPosition.getColumn()){
                                    return true;
                                }
                            }
                        }
                    }
                }
                c += 1;
            }
            r += 1;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     * isInCheckmate: Returns true if the given team has no way to protect their king from being captured
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            int r = 1;
            ChessBoard board = getChessBoard();
            for(ChessPiece[] row: board.getBoard()) {
                int c = 1;
                for (ChessPiece piece : row) {
                    if (piece != null) { // if there is a piece there ...
                        if (piece.getTeamColor() == teamColor) { // and it is your piece
                            Collection<ChessMove> piecesMoves = piecesMoves(new ChessPosition(r,c));
                            // does any of the valid moves get you out of check?
                            for(ChessMove move : piecesMoves) {
                                // perform the move
                                ChessPiece tempPiece = board.getPiece(move.getEndPosition());
                                getChessBoard().addPiece(move.getEndPosition(),piece);
                                getChessBoard().addPiece(move.getStartPosition(), null);
                                if(!isInCheck(piece.getTeamColor())){ // the move got the team out of check
                                    getChessBoard().addPiece(move.getStartPosition(),piece);
                                    getChessBoard().addPiece(move.getEndPosition(), tempPiece);
                                    return false;
                                }else{ // naw your still in check
                                    // undo the move
                                    getChessBoard().addPiece(move.getStartPosition(),piece);
                                    getChessBoard().addPiece(move.getEndPosition(), tempPiece);
                                }
                            }
                        }
                    }
                    c += 1;
                }
                r += 1;
            }
            return true; // otherwise you are in checkmate.
        }else{ // if they are not in check
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     * isInStalemate: Returns true if the given team has no legal moves, and it is currently that team’s turn
     */
    public boolean isInStalemate(TeamColor teamColor) {
        int r = 1;
        ChessBoard board = getChessBoard();
        for(ChessPiece[] row: board.getBoard()) {
            int c = 1;
            for (ChessPiece piece : row) {
                if (piece != null) { // if there is a piece there ...
                    if (piece.getTeamColor() == teamColor && teamColor == getTeamTurn()) { // and it is your piece on your turn
                        Collection<ChessMove> piecesMoves = piecesMoves(new ChessPosition(r,c));
                        // are any of the moves valid?
                        for(ChessMove move : piecesMoves){
                            // perform the move
                            getChessBoard().addPiece(move.getEndPosition(),piece);
                            getChessBoard().addPiece(move.getStartPosition(), null);
                            boolean inCheck = isInCheck(teamColor);
                            if(!inCheck) {
                                getChessBoard().addPiece(move.getStartPosition(), piece);
                                getChessBoard().addPiece(move.getEndPosition(), null);
                                return false;
                            }
                            getChessBoard().addPiece(move.getStartPosition(), piece);
                            getChessBoard().addPiece(move.getEndPosition(), null);
                        }
                    }
                }
                c += 1;
            }
            r += 1;
        }
        if(getTeamTurn() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }else{
            setTeamTurn(TeamColor.BLACK);
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getChessBoard() {
        return board;
    }
    public ChessBoard getBoard(){return board;}

    /**
     * this finds the location of this teams
     * @param color of the king we want to find
     * @return position object of the king
     */
    private ChessPosition getKingPosition(ChessGame.TeamColor color){
        int r = 1;
        ChessBoard board = getChessBoard();
        for(ChessPiece[] row : board.getBoard()) {
            int c = 1;
            for (ChessPiece piece : row) {
                if (piece != null) {
                    if (piece.getTeamColor() == color) {
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                            return new ChessPosition(r,c);
                        }
                    }
                }
                c += 1;
            }
            r += 1;
        }
        return new ChessPosition(-1,-1);
    }


    private void promotePawns(ChessMove move, TeamColor color){
        if(move.getPromotionPiece() != null){
            if(move.getEndPosition().getRow() == 8 ||
                    move.getEndPosition().getRow() == 1){
                getChessBoard().addPiece(move.getEndPosition(), new ChessPiece(color, move.getPromotionPiece()));
            }
        }
    }
}
