package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;
    public ChessPosition(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }
    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    /**
     * overriding the toString method to see the position in format (r,c)
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("(");
        str.append(row);
        str.append(",");
        str.append(col);
        str.append(")");
        return str.toString();
    }
}
