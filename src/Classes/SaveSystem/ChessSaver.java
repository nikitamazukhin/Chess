package Classes.SaveSystem;

import Classes.ChessPieces.ChessPiece;
import Classes.ChessBoard;

import java.io.FileOutputStream;
import java.io.IOException;

public class ChessSaver {

    public static int pieceTypeInfo(ChessPiece p1) {
        return switch (p1.getSymbol()) {
            case '♔', '♚' -> 1;
            case '♕', '♛' -> 2;
            case '♖', '♜' -> 3;
            case '♗', '♝' -> 4;
            case '♘', '♞' -> 5;
            case '♙', '♟' -> 0;

            default -> throw new IllegalStateException("Unexpected value: " + p1.getSymbol());
        };
    }

    public static int pieceXInfo(ChessPiece p1) {
        return p1.getX();
    }

    public static int pieceYInfo(ChessPiece p1) {
        return p1.getY();
    }

    public static int pieceColorInfo(ChessPiece p1) {
        if (p1.isWhite())
            return 0;
        else
            return 1;
    }

    public static int saveShiftValue(int counter) {
        return switch (counter) {
            case 0 -> 0;
            case 1 -> 3;
            case 2 -> 7;
            case 3 -> 11;

            default -> throw new IllegalStateException("Unexpected value: " + counter);
        };
    }

    public static void addPadding(int number, FileOutputStream outStr) throws IOException {
        outStr.write(number >> 8);
    }

    public static int pieceSaver(ChessPiece p1) {
        int toWrite = 0;
        int counter = 0;

        toWrite += pieceTypeInfo(p1) << saveShiftValue(counter);
        counter++;

        toWrite += pieceXInfo(p1) << saveShiftValue(counter);
        counter++;

        toWrite += pieceYInfo(p1) << saveShiftValue(counter);
        counter++;

        toWrite += pieceColorInfo(p1) << saveShiftValue(counter);

        return toWrite;
    }

    public static void boardSaver(ChessBoard board, String filename) throws IOException {
        try (FileOutputStream outStr = new FileOutputStream(filename + ".bin")) {
            for (ChessPiece piece : board.getPieces()) {
                addPadding(pieceSaver(piece), outStr);
                outStr.write(pieceSaver(piece));
            }
        }
    }
}
