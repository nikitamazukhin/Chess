package Classes;

import Classes.ChessPieces.*;

import java.util.ArrayList;

public class ChessBoard {
    private ArrayList<ChessPiece> pieces = new ArrayList<>();

    public ChessBoard() {
        for (int j = 1; j < 9; j++) {
            for (int i = 1; i < 9; i++) {
                switch (j) {
                    case 1 -> pieces.add(whiteFlankPieces(i, j));
                    case 2 -> pieces.add(new Pawn(true, i, j));
                    case 7 -> pieces.add(new Pawn(false, i, j));
                    case 8 -> pieces.add(blackFlankPieces(i, j));
                }
            }
        }
    }

    public ChessBoard(ArrayList<ChessPiece> pieces) {
        this.pieces = pieces;
    }

    public ArrayList<ChessPiece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<ChessPiece> pieces) {
        this.pieces = pieces;
    }

    public ChessPiece whiteFlankPieces(int x, int y) {
        return switch (x) {
            case 1, 8 -> new Rook(true, x, y);
            case 2, 7 -> new Knight(true, x, y);
            case 3, 6 -> new Bishop(true, x, y);
            case 4    -> new Queen(true, x, y);
            case 5    -> new King(true, x, y);

            default -> throw new IllegalStateException("Unexpected value: " + x);
        };
    }

    public ChessPiece blackFlankPieces(int x, int y) {
        return switch (x) {
            case 1, 8 -> new Rook(false, x, y);
            case 2, 7 -> new Knight(false, x, y);
            case 3, 6 -> new Bishop(false, x, y);
            case 4    -> new Queen(false, x, y);
            case 5    -> new King(false, x, y);

            default -> throw new IllegalStateException("Unexpected value: " + x);
        };
    }

    public static ChessPiece getPieceOnCoords(ArrayList<ChessPiece> pieces, int x, int y) {
        for (ChessPiece piece : pieces) {
            if (piece.getX() == x && piece.getY() == y)
                return piece;
        }
        return null;
    }

    public static ChessPiece getPieceWithSymbol(ArrayList<ChessPiece> pieces, char symbol) {
        for (ChessPiece piece : pieces) {
            if (piece.getSymbol() == symbol)
                return piece;
        }
        return null;
    }

    public static King getKing(ArrayList<ChessPiece> pieces, boolean isWhite) {
        return (King) (isWhite ? getPieceWithSymbol(pieces, '♔') : getPieceWithSymbol(pieces, '♚'));
    }
    public static ArrayList<ChessPiece> clonePieces(ArrayList<ChessPiece> pieces) {
        ArrayList<ChessPiece> result = new ArrayList<>(pieces.size());

        for (ChessPiece piece : pieces)
            result.add(piece.clone());

        return result;
    }

    public static ChessPiece getCheckingPiece(ArrayList<ChessPiece> pieces, King king) {
        for (ChessPiece piece : pieces) {
            if (piece.isWhite() != king.isWhite() && piece.moveIsLegal(pieces, king.getX(), king.getY())) {
                king.setInCheck(true);
                return piece;
            }
        }
        return null;
    }

    public static ArrayList<ChessPiece> getCheckingPieceList(ArrayList<ChessPiece> pieces, King king) {
        ArrayList<ChessPiece> result = new ArrayList<>();
        ChessPiece pieceToAdd = getCheckingPiece(pieces, king);

        if (pieceToAdd != null) {
            while(!result.contains(pieceToAdd)) {
                result.add(pieceToAdd);
                pieceToAdd = getCheckingPiece(pieces, king);
            }
        }

        return result;
    }
}
