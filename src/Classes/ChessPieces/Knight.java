package Classes.ChessPieces;

public class Knight extends ChessPiece {

    public Knight(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♘');
        else setSymbol('♞');
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int xAbsVal = Math.abs(getDiff.operate(newX, getX()));
        int yAbsVal = Math.abs(getDiff.operate(newY, getY()));

        return xAbsVal + yAbsVal == 3 && xAbsVal != 0 && yAbsVal != 0;
    }
}