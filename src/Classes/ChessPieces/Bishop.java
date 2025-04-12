package Classes.ChessPieces;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    public Bishop(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♗');
        else setSymbol('♝');
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int xAbsVal = Math.abs(getDiff.operate(newX, getX()));
        int yAbsVal = Math.abs(getDiff.operate(newY, getY()));

        return xAbsVal == yAbsVal && xAbsVal != 0;
    }

    @Override
    public boolean isReachable(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int xDiff = getDiff.operate(newX, getX());
        int yDiff = getDiff.operate(newY, getY());

        if (xDiff > 0 && yDiff > 0) return isClearOnNE(pieces, newX, newY);
        if (xDiff < 0 && yDiff > 0) return isClearOnNW(pieces, newX, newY);
        if (xDiff > 0 && yDiff < 0) return isClearOnSE(pieces, newX, newY);
        if (xDiff < 0 && yDiff < 0) return isClearOnSW(pieces, newX, newY);

        return false;
    }
}
