package Interfaces;

import Classes.ChessBoard;
import Classes.ChessPieces.ChessPiece;
import Classes.ChessPieces.King;

import java.util.ArrayList;

public interface CastlingCapable {
    boolean castlingReachable(ArrayList<ChessPiece> pieces, int newX);
    boolean castlingIsLegal(ArrayList<ChessPiece> pieces, int newX, int newY);
    boolean isCastlingCompatible(ChessPiece p1);
    void castling(ArrayList<ChessPiece> pieces, int newX, int newY);
    static boolean castlingSafeOnRight(ArrayList<ChessPiece> pieces, King king) {
        return     ChessBoard.getCheckingPiece(pieces, new King(king.isWhite(), king.getX() + 1, king.getY())) == null
                && ChessBoard.getCheckingPiece(pieces, new King(king.isWhite(), king.getX() + 2, king.getY())) == null;
    }
    static boolean castlingSafeOnLeft(ArrayList<ChessPiece> pieces, King king) {
        return     ChessBoard.getCheckingPiece(pieces, new King(king.isWhite(), king.getX() - 1, king.getY())) == null
                && ChessBoard.getCheckingPiece(pieces, new King(king.isWhite(), king.getX() - 2, king.getY())) == null;
    }
}