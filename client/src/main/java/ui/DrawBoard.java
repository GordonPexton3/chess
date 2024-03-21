package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;
public class DrawBoard {

    private final String[] rows = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
    private final ChessPiece[][] board;

    public DrawBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        board = chessBoard.getBoard();

        drawRedUpBoard(out);
        drawBlueUpBoard(out);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void drawRedUpBoard(PrintStream out) {
        out.println(SET_BG_COLOR_BLACK);
        borderHtoA(out);
        for (int r = 0; r <= 7 ; r++){
            drawRow(out,r,true);
        }
        borderHtoA(out);
        out.println(SET_BG_COLOR_BLACK);

    }

    private void drawBlueUpBoard(PrintStream out) {
        borderAtoH(out);
        for (int r = 0; r <= 7 ; r++){
            drawRow(out,r,false);
        }
        borderAtoH(out);
        out.println(SET_BG_COLOR_BLACK);
    }

    private void drawRow(PrintStream out, int r, boolean redUp) {
        String squareColor;
        ChessPiece[][] chessBoard = alignBoard(redUp);
        printEdge(out, redUp, r);
        boolean toggle;
        toggle = r % 2 == 1;
        for(int c = 0; c <= 7; c++){
            if(toggle){
                squareColor = SET_BG_COLOR_WHITE;
                toggle = false;
            }else{
                squareColor = SET_BG_COLOR_BLACK;
                toggle = true;
            }
            printPiece(out,squareColor, chessBoard, r, c);
        }
        printEdge(out, redUp, r);
        out.println(SET_BG_COLOR_BLACK);
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
