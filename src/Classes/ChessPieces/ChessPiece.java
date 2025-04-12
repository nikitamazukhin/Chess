package Classes.ChessPieces;

import Classes.ChessBoard;
import Interfaces.ChessMovable;
import Interfaces.CoordinateConfirmation;
import Interfaces.CoordinateOperation;

import java.util.ArrayList;

public abstract class ChessPiece implements ChessMovable, Cloneable {
    private boolean isWhite;
    private char symbol;
    private int x;

    private int y;

    public ChessPiece(boolean isWhite, int x, int y) {
        this.setWhite(isWhite);
        this.x = x;
        this.y = y;
    }

    CoordinateOperation getDiff = (int newCoord, int coord) -> newCoord - coord;

    CoordinateConfirmation coordsAreLegal = (int newX, int newY) -> (newX >= 1 && newX <= 8) && (newY >= 1 && newY <= 8);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract boolean distIsLegal(int newX, int newY);

    public boolean isReachable(ArrayList<ChessPiece> pieces, int newX, int newY) {
        return true;
    }

    public boolean isClearOnUp(ArrayList<ChessPiece> pieces, int newY) {
        int nextY = y + 1;

        while (nextY < newY) {
            if (ChessBoard.getPieceOnCoords(pieces, x, nextY) != null)
                return false;

            nextY++;
        }
        return true;
    }

    public boolean isClearOnDown(ArrayList<ChessPiece> pieces, int newY) {
        int nextY = y - 1;
        while (nextY > newY) {
            if (ChessBoard.getPieceOnCoords(pieces, x, nextY) != null)
                return false;

            nextY--;
        }
        return true;
    }

    public boolean isClearOnRight(ArrayList<ChessPiece> pieces, int newX) {
        int nextX = x + 1;

        while (nextX < newX) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, y) != null)
                return false;

            nextX++;
        }
        return true;
    }

    public boolean isClearOnLeft(ArrayList<ChessPiece> pieces, int newX) {
        int nextX = x - 1;

        while (nextX > newX) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, y) != null)
                return false;

            nextX--;
        }
        return true;
    }

    public boolean isClearOnNE(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int nextX = x + 1;
        int nextY = y + 1;

        while ((nextX < newX) && (nextY < newY)) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, nextY) != null)
                return false;

            nextX++;
            nextY++;
        }
        return true;
    }

    public boolean isClearOnNW(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int nextX = x - 1;
        int nextY = y + 1;

        while ((nextX > newX) && (nextY < newY)) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, nextY) != null)
                return false;

            nextX--;
            nextY++;
        }
        return true;
    }

    public boolean isClearOnSE(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int nextX = x + 1;
        int nextY = y - 1;

        while ((nextX < newX) && (nextY > newY)) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, nextY) != null)
                return false;

            nextX++;
            nextY--;
        }
        return true;
    }

    public boolean isClearOnSW(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int nextX = x - 1;
        int nextY = y - 1;

        while ((nextX > newX) && (nextY > newY)) {
            if (ChessBoard.getPieceOnCoords(pieces, nextX, nextY) != null)
                return false;

            nextX--;
            nextY--;
        }
        return true;
    }

    public boolean moveIsLegal(ArrayList<ChessPiece> pieces, int newX, int newY) {
        return coordsAreLegal.confirm(newX, newY) && distIsLegal(newX, newY) && isReachable(pieces, newX, newY);
    }

    public void capture(ChessPiece p1) {
        p1.x = 0;
        p1.y = 0;
    }

    public void moveAction(ArrayList<ChessPiece> pieces, int newX, int newY) {
        ChessPiece pieceOnNewCoord = ChessBoard.getPieceOnCoords(pieces, newX, newY);

        if (pieceOnNewCoord != null && pieceOnNewCoord.isWhite() != isWhite()) {
            capture(pieceOnNewCoord);
            x = newX;
            y = newY;
        }
        else if (pieceOnNewCoord == null) {
            x = newX;
            y = newY;
        }
    }

    public void move(ArrayList<ChessPiece> pieces, int newX, int newY) {
        if (moveIsLegal(pieces, newX, newY)) {
            moveAction(pieces, newX, newY);
        }
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}