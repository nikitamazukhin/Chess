package Classes.ChessPieces;

import Classes.ChessPieces.ChessPiece;

import java.util.ArrayList;

public abstract class MoveSensitiveChessPiece extends ChessPiece {
    boolean isUnmoved = true;

    public MoveSensitiveChessPiece(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
    }

    public abstract boolean distIsLegal(int newX, int newY);

    @Override
    public void move(ArrayList<ChessPiece> pieces, int newX, int newY) {
        if (moveIsLegal(pieces, newX, newY)) {
            moveAction(pieces, newX, newY);
            isUnmoved = false;
        }
    }
}
