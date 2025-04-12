package Classes.ChessPieces;

import Classes.ChessBoard;
import Interfaces.CastlingCapable;

import java.util.ArrayList;

public class King extends MoveSensitiveChessPiece implements CastlingCapable {
    private boolean inCheck = false;

    public King(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♔');
        else setSymbol('♚');
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int xAbsVal = Math.abs(getDiff.operate(newX, getX()));
        int yAbsVal = Math.abs(getDiff.operate(newY, getY()));

        return xAbsVal + yAbsVal == 1 || (xAbsVal == 1 && yAbsVal == 1);
    }

    public boolean castlingReachable(ArrayList<ChessPiece> pieces, int newX) {
        int xDiff = getDiff.operate(newX, getX());

        return xDiff > 0 ? isClearOnRight(pieces, newX) : isClearOnLeft(pieces, newX);
    }

    public boolean castlingIsLegal(ArrayList<ChessPiece> pieces, int newX, int newY) {
        return !inCheck && isUnmoved && coordsAreLegal.confirm(newX, newY) && castlingReachable(pieces, newX);
    }

    public boolean isCastlingCompatible(ChessPiece p1) {
        return p1.isWhite() == isWhite() && (p1.getSymbol() == '♖' || p1.getSymbol() == '♜')
                && ((MoveSensitiveChessPiece) p1).isUnmoved;
    }

    @Override
    public void castling(ArrayList<ChessPiece> pieces, int newX, int newY) {
        if (castlingIsLegal(pieces, newX, newY)) {
            Rook swapTarget = (Rook) ChessBoard.getPieceOnCoords(pieces, newX, newY);

            if (swapTarget != null && isCastlingCompatible(swapTarget)) {
                int xDiff = getDiff.operate(newX, getX());

                if (xDiff > 0) {
                    if (CastlingCapable.castlingSafeOnRight(pieces, this)) {
                        setX(getX() + 2);
                        swapTarget.setX(getX() - 2);
                        isUnmoved = false;
                        swapTarget.isUnmoved = false;
                    }
                }
                else {
                    if (CastlingCapable.castlingSafeOnLeft(pieces, this)) {
                        setX(getX() - 2);
                        swapTarget.setX(getX() + 3);
                        isUnmoved = false;
                        swapTarget.isUnmoved = false;
                    }
                }
            }
        }
    }
}
