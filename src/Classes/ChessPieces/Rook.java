package Classes.ChessPieces;

import Classes.ChessBoard;
import Interfaces.CastlingCapable;

import java.util.ArrayList;

public class Rook extends MoveSensitiveChessPiece implements CastlingCapable {

    public Rook(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♖');
        else setSymbol('♜');
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int xAbsVal = Math.abs(getDiff.operate(newX, getX()));
        int yAbsVal = Math.abs(getDiff.operate(newY, getY()));
        return (xAbsVal >= 1 && yAbsVal == 0) || (yAbsVal >= 1 && xAbsVal == 0);
    }

    @Override
    public boolean isReachable(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int xDiff = getDiff.operate(newX, getX());
        int yDiff = getDiff.operate(newY, getY());

        if (xDiff != 0)
            return xDiff > 0 ? isClearOnRight(pieces, newX) : isClearOnLeft(pieces, newX);
        else
            return yDiff > 0 ? isClearOnUp(pieces, newY) : isClearOnDown(pieces, newY);
    }

    public boolean castlingReachable(ArrayList<ChessPiece> pieces, int newX) {
        int xDiff = getDiff.operate(newX, getX());

        return xDiff > 0 ? isClearOnRight(pieces, newX) : isClearOnLeft(pieces, newX);
    }

    public boolean castlingIsLegal(ArrayList<ChessPiece> pieces, int newX, int newY) {
        return isUnmoved && coordsAreLegal.confirm(newX, newY) && castlingReachable(pieces, newX);
    }

    public boolean isCastlingCompatible(ChessPiece p1) {
        return p1.isWhite() == isWhite() && (p1.getSymbol() == '♔' || p1.getSymbol() == '♚')
                && !((King) p1).isInCheck() && ((King) p1).isUnmoved;
    }

    @Override
    public void castling(ArrayList<ChessPiece> pieces, int newX, int newY) {
        if (castlingIsLegal(pieces, newX, newY)) {
            King swapTarget = (King) ChessBoard.getPieceOnCoords(pieces, newX, newY);

            if (swapTarget != null && isCastlingCompatible(swapTarget)) {
                int xDiff = getDiff.operate(newX, getX());

                if (xDiff > 0) {
                    if (CastlingCapable.castlingSafeOnLeft(pieces, swapTarget)) {
                        setX(getX() + 3);
                        swapTarget.setX(getX() - 2);
                        isUnmoved = false;
                        swapTarget.isUnmoved = false;
                    }
                }
                else {
                    if (CastlingCapable.castlingSafeOnRight(pieces, swapTarget)) {
                        setX(getX() - 2);
                        swapTarget.setX(getX() + 2);
                        isUnmoved = false;
                        swapTarget.isUnmoved = false;
                    }
                }
            }
        }
    }
}
