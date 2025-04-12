package Classes.SaveSystem;

import Classes.ChessPieces.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChessReader {

    public static int readTypeInfo(int pieceInfo) {
        int infoBitCount = 3;
        int behindBitCount = 0;
        int bitsToKeepFirst = infoBitCount + behindBitCount;

        return (pieceInfo << 32 - bitsToKeepFirst) >>> 32 - infoBitCount;
    }

    public static int readXInfo(int pieceInfo) {
        int infoBitCount = 4;
        int behindBitCount = 3;
        int bitsToKeepFirst = infoBitCount + behindBitCount;

        return (pieceInfo << 32 - bitsToKeepFirst) >>> 32 - infoBitCount;
    }

    public static int readYInfo(int pieceInfo) {
        int infoBitCount = 4;
        int behindBitCount = 7;
        int bitsToKeepFirst = infoBitCount + behindBitCount;

        return (pieceInfo << 32 - bitsToKeepFirst) >>> 32 - infoBitCount;
    }

    public static boolean readColor(int pieceInfo) {
        return pieceInfo >>> 11 == 0;
    }

    public static ChessPiece readPiece (int pieceInfo) {
        return switch (readTypeInfo(pieceInfo)) {
            case 1 -> new King(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));
            case 2 -> new Queen(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));
            case 3 -> new Rook(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));
            case 4 -> new Bishop(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));
            case 5 -> new Knight(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));
            case 0 -> new Pawn(readColor(pieceInfo), readXInfo(pieceInfo), readYInfo(pieceInfo));

            default -> throw new IllegalStateException("Unexpected value: " + readTypeInfo(pieceInfo));
        };
    }

    public static int byteArrToInt (byte[] arr) {
        return ((arr[0] & 0xFF) << 8) | ((arr[1] & 0xFF));
    }

    public static ArrayList<ChessPiece> boardReader(String filename) throws IOException {
        ArrayList<ChessPiece> result = new ArrayList<>();

        try (FileInputStream inStr = new FileInputStream(filename)) {
            byte[] i = inStr.readNBytes(2);
            int value;

            while(i.length != 0) {
                value = byteArrToInt(i);
                result.add(readPiece(value));
                i = inStr.readNBytes(2);
            }

        }
        return result;
    }
}
