package Classes.ChessPieces;

import Classes.ChessBoard;

import java.util.ArrayList;
import java.util.Scanner;

public class Pawn extends MoveSensitiveChessPiece {

    public Pawn(boolean isWhite, int x, int y) {
        super(isWhite, x, y);
        if (isWhite) setSymbol('♙');
        else setSymbol('♟');
    }

    @Override
    public boolean distIsLegal(int newX, int newY) {
        int yDiff = getDiff.operate(newY, getY());

        if (isWhite()) {
            if (isUnmoved) return newX == getX() && (yDiff == 1 || yDiff == 2);
            return newX == getX() && yDiff == 1;
        }

        else {
            if (isUnmoved) return newX == getX() && (yDiff == -1 || yDiff == -2);
            return newX == getX() && yDiff == -1;
        }
    }

    @Override
    public boolean isReachable(ArrayList<ChessPiece> pieces, int newX, int newY) {
        int yDiff = getDiff.operate(newY, getY());
        return yDiff > 0 ? isClearOnUp(pieces, newY) : isClearOnDown(pieces, newY);
    }

    @Override
    public void moveAction(ArrayList<ChessPiece> pieces, int newX, int newY) {
        ChessPiece pieceOnNewCoord = ChessBoard.getPieceOnCoords(pieces, newX, newY);

        if (pieceOnNewCoord == null) {
            setX(newX);
            setY(newY);
        }
    }

    public boolean pawnCaptureDistIsLegal(int newX, int newY) {
        int xDiff = getDiff.operate(newX, getX());
        int xDiffAbs = Math.abs(xDiff);
        int yDiff = getDiff.operate(newY, getY());

        if (isWhite()) {
            return xDiffAbs == 1 && yDiff == 2;
        }

        else {
            return xDiffAbs == 1 && yDiff == -2;
        }
    }

    public boolean pawnCaptureIsLegal(int newX, int newY) {
        return coordsAreLegal.confirm(newX, newY) && pawnCaptureDistIsLegal(newX, newY);
    }

    public ChessPiece pawnCaptureTarget(ArrayList<ChessPiece> pieces, int newX, int newY) {
        return isWhite() ?
                ChessBoard.getPieceOnCoords(pieces, newX, newY - 1) :
                ChessBoard.getPieceOnCoords(pieces, newX, newY + 1);
    }

    public void pawnCapture(ArrayList<ChessPiece> pieces, int newX, int newY) {
        ChessPiece pieceOnNewCoord = ChessBoard.getPieceOnCoords(pieces, newX, newY);
        ChessPiece target = pawnCaptureTarget(pieces, newX, newY);

        if (pieceOnNewCoord == null && target != null && target.isWhite() != isWhite()) {
            capture(target);
            setX(newX);
            setY(newY);
        }
    }

    @Override
    public void move(ArrayList<ChessPiece> pieces, int newX, int newY) {
        if (moveIsLegal(pieces, newX, newY)) {
            moveAction(pieces, newX, newY);
            isUnmoved = false;
        }
        else if (pawnCaptureIsLegal(newX, newY)) {
            pawnCapture(pieces, newX, newY);
            isUnmoved = false;
        }
    }

    public boolean enPassantDistIsLegal(int newX, int newY) {
        int xDiff = getDiff.operate(newX, getX());
        int xDiffAbs = Math.abs(xDiff);
        int yDiff = getDiff.operate(newY, getY());

        if (isWhite()) {
            return xDiffAbs == 1 && yDiff == 1;
        }

        else {
            return xDiffAbs == 1 && yDiff == -1;
        }
    }

    public boolean enPassantIsLegal(ArrayList<ChessPiece> pieces, int turnCount, int black2TileTurnCount, int black2TilePawnX, int black2TilePawnY, int newX, int newY) {
        ChessPiece enPassantTarget = pawnCaptureTarget(pieces, newX, newY);

        return
                turnCount == black2TileTurnCount + 1
                        && (enPassantTarget.getX() == black2TilePawnX && enPassantTarget.getY() == black2TilePawnY)
                        && coordsAreLegal.confirm(newX, newY)
                        && enPassantDistIsLegal(newX, newY);
    }

    public void enPassant(ArrayList<ChessPiece> pieces, int turnCount, int pawn2TileTurnCount, int pawn2TileX, int pawn2TileY, int newX, int newY) {
        if (enPassantIsLegal(pieces, turnCount, pawn2TileTurnCount, pawn2TileX, pawn2TileY, newX, newY))
            pawnCapture(pieces, newX, newY);
    }

    public static void promote(ArrayList<ChessPiece> pieces, ChessPiece p1) {
        Scanner scan = new Scanner(System.in);
        boolean isPromoted = false;

        while (!isPromoted) {
            System.out.println("Promote pawn to which piece?");
            String toPiece = scan.next();

            switch (toPiece) {
                case "bishop" -> {
                    pieces.add(new Bishop(p1.isWhite(), p1.getX(), p1.getY()));
                    isPromoted = true;
                }
                case "knight" -> {
                    pieces.add(new Knight(p1.isWhite(), p1.getX(), p1.getY()));
                    isPromoted = true;
                }
                case "queen"  -> {
                    pieces.add(new Queen(p1.isWhite(), p1.getX(), p1.getY()));
                    isPromoted = true;
                }
                case "rook"   -> {
                    pieces.add(new Rook(p1.isWhite(), p1.getX(), p1.getY()));
                    isPromoted = true;
                }
            }
        }
        p1.setX(0);
        p1.setY(0);
    }
}
