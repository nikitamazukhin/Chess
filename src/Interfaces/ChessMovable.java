package Interfaces;

import Classes.ChessPieces.ChessPiece;

import java.util.ArrayList;

public interface ChessMovable {
    void moveAction(ArrayList<ChessPiece> pieces, int newX, int newY);
    void move(ArrayList<ChessPiece> pieces, int newX, int newY);
}
