package chess;

import java.util.Collection;
import java.util.HashSet;

public class PieceMoves {

    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        // up and to the right
        for(int r = r0 + 1, c = c0 + 1; board.onBoard(r,c); r ++, c ++){
            // if there is no piece there, add it
            if(board.getPieceAtIndex(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                // if the piece is an enemy, take it and stop adding moves
            }else if(board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                break;
                // if the piece is one of our own then don't add the place and break
            }else if(board.getPieceAtIndex(r,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // up and to the left
        for(int r = r0 + 1, c = c0 - 1; board.onBoard(r,c); r ++, c --){
            // if there is no piece there, add it
            if(board.getPieceAtIndex(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                // if the piece is an enemy, take it and stop adding moves
            }else if(board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                break;
                // if the piece is one of our own then don't add the place and break
            }else if(board.getPieceAtIndex(r,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // down and to the right
        for(int r = r0 - 1, c = c0 + 1; board.onBoard(r,c); r --, c ++){
            // if there is no piece there, add it
            if(board.getPieceAtIndex(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                // if the piece is an enemy, take it and stop adding moves
            }else if(board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                break;
                // if the piece is one of our own then don't add the place and break
            }else if(board.getPieceAtIndex(r,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // down and to the left
        for(int r = r0 - 1, c = c0 - 1; board.onBoard(r,c); r --, c --){
            // if there is no piece there, add it
            if(board.getPieceAtIndex(r,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                // if the piece is an enemy, take it and stop adding moves
            }else if(board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                break;
                // if the piece is one of our own then don't add the place and break
            }else if(board.getPieceAtIndex(r,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        return moves;
    }

    public static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        ChessPosition[] positions = {new ChessPosition(2,-1), new ChessPosition(2,1),
                new ChessPosition(-2,-1), new ChessPosition(-2,1),
                new ChessPosition(1,2), new ChessPosition(-1,2),
                new ChessPosition(1,-2), new ChessPosition(-1,-2)};
        for(ChessPosition pos : positions){
            int r = r0 + pos.getRow();
            int c = c0 + pos.getColumn();
            //if is on board and it is null or if occupied by an enemy then add the move
            if(board.onBoard(r,c) &&
                    (board.getPieceAtIndex(r,c) == null ||
                            board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor())){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
            }
        }
        return moves;
    }

    public static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        for(int r = r0 + 1; r > r0 - 2; r --){
            for(int c = c0 - 1; c < c0 + 2; c ++){
                // if the piece is on the board and not the kings place and is null or not this team peace
                // then you can have the place
                if(board.onBoard(r,c) && (r != r0 || c != c0) && (board.getPieceAtIndex(r,c) == null ||
                        board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor())){
                    moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                }
            }
        }
        return moves;
    }

    public static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        moves.addAll(bishopMoves(board,myPosition));
        moves.addAll(rookMoves(board,myPosition));
        return moves;
    }

    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        ChessPiece thisPiece = board.getPiece(myPosition);
        if(thisPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            return blackPawnMoves(board, myPosition);
        }else{
            return whitePawnMoves(board, myPosition);
        }
    }

    private static Collection<ChessMove> blackPawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        // if this is the starting position can he move foreword two?
        if(r0 == 7 && board.getPieceAtIndex(r0 - 1, c0) == null &&
                board.getPieceAtIndex(r0 - 2, c0) == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(r0 - 2, c0), null));
        }
        // assemble list of positions to try
        ChessPosition[] positions = {new ChessPosition(-1,-1),
                new ChessPosition(-1,0), new ChessPosition(-1,1)};
        // looping through positions
        for(ChessPosition pos : positions){
            int r = r0 + pos.getRow();
            int c = c0 + pos.getColumn();
            // if the place doesn on exist, continue
            if(!board.onBoard(r,c)){continue;}
            // if moving foreword
            if(c == c0){
                // if unoccupied
                if(board.getPieceAtIndex(r,c) == null){
                    // if you reach the other side
                    if(r == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.QUEEN));
                    }else{// if you do not reach the other side
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                    }
                }
                // if moving diagonal
            }else{
                // if the place is not empty and there is an enemy there
                if(board.getPieceAtIndex(r,c) != null &&
                        board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                    // if you reach the other side
                    if(r == 1) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.QUEEN));
                    }else{// if you do not reach the other side
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                    }
                }
            }
        }
        return moves;
    }

    private static Collection<ChessMove> whitePawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        // if this is the starting position can he move foreword two?
        if(r0 == 2 && board.getPieceAtIndex(r0 + 1, c0) == null &&
                board.getPieceAtIndex(r0 + 2, c0) == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(r0 + 2, c0), null));
        }
        // assemble list of positions to try
        ChessPosition[] positions = {new ChessPosition(1,-1),
                new ChessPosition(1,0), new ChessPosition(1,1)};
        // looping through positions
        for(ChessPosition pos : positions){
            int r = r0 + pos.getRow();
            int c = c0 + pos.getColumn();
            // if the place doesn't on exist, continue
            if(!board.onBoard(r,c)){continue;}
            // if moving foreword
            if(c == c0){
                // if unoccupied
                if(board.getPieceAtIndex(r,c) == null){
                    // if you reach the other side
                    if(r == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.QUEEN));
                    }else{// if you do not reach the other side
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                    }
                }
                // if moving diagonal
            }else{
                // if the place is not empty and there is an enemy there
                if(board.getPieceAtIndex(r,c) != null &&
                        board.getPieceAtIndex(r,c).getTeamColor() != thisPiece.getTeamColor()){
                    // if you reach the other side
                    if(r == 8) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), ChessPiece.PieceType.QUEEN));
                    }else{// if you do not reach the other side
                        moves.add(new ChessMove(myPosition, new ChessPosition(r,c), null));
                    }
                }
            }
        }
        return moves;
    }

    public static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<>();
        ChessPiece thisPiece = board.getPiece(myPosition);
        int r0 = myPosition.getRow();
        int c0 = myPosition.getColumn();
        // moving to the right
        for(int c = c0 + 1; board.onBoard(r0,c); c ++){
            // if the place is unoccupied take it
            if(board.getPieceAtIndex(r0,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0,c), null));
                // if the place is occupied by an enemy take it and break
            }else if(board.getPieceAtIndex(r0,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0,c), null));
                break;
                // if the place is occupied by a friendly break
            }else if(board.getPieceAtIndex(r0,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // moving to the left
        for(int c = c0 - 1; board.onBoard(r0,c); c --){
            // if the place is unoccupied take it
            if(board.getPieceAtIndex(r0,c) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0,c), null));
                // if the place is occupied by an enemy take it and break
            }else if(board.getPieceAtIndex(r0,c).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r0,c), null));
                break;
                // if the place is occupied by a friendly break
            }else if(board.getPieceAtIndex(r0,c).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // moving up
        for(int r = r0 + 1; board.onBoard(r,c0); r ++){
            // if the place is unoccupied take it
            if(board.getPieceAtIndex(r,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c0), null));
                // if the place is occupied by an enemy take it and break
            }else if(board.getPieceAtIndex(r,c0).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c0), null));
                break;
                // if the place is occupied by a friendly break
            }else if(board.getPieceAtIndex(r,c0).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        // moving down
        for(int r = r0 - 1; board.onBoard(r,c0); r --){
            // if the place is unoccupied take it
            if(board.getPieceAtIndex(r,c0) == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c0), null));
                // if the place is occupied by an enemy take it and break
            }else if(board.getPieceAtIndex(r,c0).getTeamColor() != thisPiece.getTeamColor()){
                moves.add(new ChessMove(myPosition, new ChessPosition(r,c0), null));
                break;
                // if the place is occupied by a friendly break
            }else if(board.getPieceAtIndex(r,c0).getTeamColor() == thisPiece.getTeamColor()){
                break;
            }
        }
        return moves;
    }
}
