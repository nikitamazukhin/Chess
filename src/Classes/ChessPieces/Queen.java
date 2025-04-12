package Classes.ChessPieces;

import java.util.ArrayList;

public class Queen extends ChessPiece {

    public Queen(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♕');
        else setSymbol('♛');
    }

    @Override
    public boolean isReachable(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int xDiff = getDiff.operate(newX, getX());
        int yDiff = getDiff.operate(newY, getY());
        int xAbsDiff = Math.abs(xDiff);
        int yAbsDiff = Math.abs(yDiff);

        if (xAbsDiff == yAbsDiff) {
            if (xDiff > 0 && yDiff > 0) return isClearOnNE(pieces, newX, newY);
            if (xDiff < 0 && yDiff > 0) return isClearOnNW(pieces, newX, newY);
            if (xDiff > 0 && yDiff < 0) return isClearOnSE(pieces, newX, newY);
            if (xDiff < 0 && yDiff < 0) return isClearOnSW(pieces, newX, newY);
        }

        else {
            if (xDiff != 0)
                return xDiff > 0 ? isClearOnRight(pieces, newX) : isClearOnLeft(pieces, newX);
            else
                return yDiff > 0 ? isClearOnUp(pieces, newY) : isClearOnDown(pieces, newY);
        }
        return false;
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int xAbsVal = Math.abs(getDiff.operate(newX, getX()));
        int yAbsVal = Math.abs(getDiff.operate(newY, getY()));
        return (xAbsVal == yAbsVal) || (xAbsVal >= 1 && yAbsVal == 0) || (yAbsVal >= 1 && xAbsVal == 0);
    }
}
