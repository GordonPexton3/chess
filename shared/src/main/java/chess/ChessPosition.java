package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    // these values should be thought of as values from 1-8 not 0-7.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
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
