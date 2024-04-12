package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;
public class DrawBoard {

    private final String[] rows = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private ChessPiece[][] board;
    private boolean drawMoves = false;
    private List<ChessPosition> piecePositions;

    public void drawBoard(ChessGame chessGame) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        this.board = chessGame.getChessBoard().getBoard();

        if(chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE){
//            drawBoardBlackPlayer(out);
            drawBoardWhitePlayer(out);
        }else if(chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK){
//            drawBoardWhitePlayer(out);
            drawBoardBlackPlayer(out);
        }
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public void drawMovesOnBoard(ChessGame chessGame, ChessPosition pos){
        drawMoves = true;
        Collection<ChessMove> moves = chessGame.piecesMoves(pos);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        this.board = chessGame.getChessBoard().getBoard();
        piecePositions = new ArrayList<>();
        for (ChessMove move: moves){
            piecePositions.add(move.getEndPosition());
        }
        if(chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE){
            invertPiecePositions();
            drawBoardWhitePlayer(out);
        }else if(chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK){
            drawBoardBlackPlayer(out);
        }
        drawMoves = false;
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void drawBoardWhitePlayer(PrintStream out) {
//        out.println(SET_BG_COLOR_BLACK);
        borderHtoA(out);
        for (int r = 0; r <= 7 ; r++){
            drawRow(out,r,true);
        }
        borderHtoA(out);
//        out.println(SET_BG_COLOR_BLACK);
    }

    private void drawBoardBlackPlayer(PrintStream out) {
//        out.println(SET_BG_COLOR_BLACK);
        borderAtoH(out);
        for (int r = 0; r <= 7 ; r++){
            drawRow(out,r,false);
        }
        borderAtoH(out);
//        out.println(SET_BG_COLOR_BLACK);
    }

    private void drawRow(PrintStream out, int r, boolean redUp) {
        String squareColor;
        ChessPiece[][] chessBoard = alignBoard(redUp);
        printEdge(out, redUp, r);
        boolean toggle;
        toggle = r % 2 == 1;
        for(int c = 7; c >= 0; c--){
            if(toggle){
                squareColor = normalOrMovesColor(SET_BG_COLOR_WHITE, r, c);
                toggle = false;
            }else{
                squareColor = normalOrMovesColor(SET_BG_COLOR_BLACK, r, c);
                toggle = true;
            }
            printPiece(out,squareColor, chessBoard, r, c);
        }
        printEdge(out, redUp, r);
        out.println(SET_BG_COLOR_BLACK);
    }

    private String normalOrMovesColor(String originalColor, int r, int c){
        if(drawMoves){
            if(piecePositions.contains(new ChessPosition(r + 1,c + 1))){
                if(originalColor.equals(SET_BG_COLOR_WHITE)){
                    return SET_BG_COLOR_GREEN;
                } else if (originalColor.equals(SET_BG_COLOR_BLACK)) {
                    return SET_BG_COLOR_DARK_GREEN;
                }
            }
        }
        return originalColor;
    }

    private void printPiece(PrintStream out, String squareColor, ChessPiece[][] chessBoard, int r, int c){
        out.print(squareColor);
        ChessPiece chessPiece = chessBoard[r][c];
        if(chessPiece == null){
            out.print("   ");
        }else if(chessPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            out.print(SET_TEXT_COLOR_RED);
            out.print(SET_TEXT_BOLD);
            out.printf(" %c ", pieceToCharacter.get(chessPiece.getPieceType()));
        }else{
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(SET_TEXT_BOLD);
            out.printf(" %c ", pieceToCharacter.get(chessPiece.getPieceType()));
        }
    }
    private void printEdge(PrintStream out, boolean redUp, int r){
        borderFont(out);
        if(redUp){
            out.printf(" %d ", r + 1);
        }else{
            out.printf(" %d ", 8 - r);
        }
    }

    private ChessPiece[][] alignBoard(boolean redUp){
        ChessPiece[][] chessBoard;
        if(redUp){
            chessBoard = invert(this.board);
        }else{
            chessBoard = this.board;
        }
        return chessBoard;
    }

    private void invertPiecePositions(){
        List<ChessPosition> newPositions = new ArrayList<>();
        for(ChessPosition pos: piecePositions){
            newPositions.add(new ChessPosition(9 - pos.getRow(), 9 - pos.getColumn()));
        }
        this.piecePositions = newPositions;

    }

    private ChessPiece[][] invert(ChessPiece[][] board) {
        ChessPiece[][] inverted = new ChessPiece[8][8];
        for(int r = 0; r <= 7; r++){
            for(int c = 0; c <= 7; c++){
                inverted[7-r][7-c] = board[r][c];
            }
        }
        return inverted;
    }
    private void borderHtoA(PrintStream out){
        borderFont(out);
        out.print("   ");
        for(int i = 7; i >= 0; i --){
            out.print(rows[i]);
        }
        out.print("   ");
        out.println(SET_BG_COLOR_BLACK);
    }

    private void borderAtoH(PrintStream out){
        borderFont(out);
        out.print("   ");
        for(int i = 0; i <= 7; i++){
            out.print(rows[i]);
        }
        out.print("   ");
        out.println(SET_BG_COLOR_BLACK);
    }

    private void borderFont(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private final Map<ChessPiece.PieceType, Character> pieceToCharacter = Map.of(
            ChessPiece.PieceType.ROOK, 'R',
            ChessPiece.PieceType.KNIGHT,'N',
            ChessPiece.PieceType.BISHOP,'B',
            ChessPiece.PieceType.QUEEN,'Q',
            ChessPiece.PieceType.KING,'K' ,
            ChessPiece.PieceType.PAWN,'P'
    );
}
