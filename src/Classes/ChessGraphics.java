package Classes;

import Classes.ChessPieces.ChessPiece;
import Interfaces.CoordinateConfirmation;

public class ChessGraphics {
    public static char drawTile(int x, int y) {
        if ((x % 2 == 0) == (y % 2 == 0))
            return '▮';
        else
            return '▯';
    }

    static CoordinateConfirmation isHeaderPos = (int x, int y) -> (y == 9 || y == 0) && x == 0;

    static CoordinateConfirmation isBorderPos = (int x, int y) -> (x == 0 || x == 9) && !(y == 9 || y == 0);

    static CoordinateConfirmation isInsideBoard = (int x, int y) -> (x >= 1 && x <= 8) && (y >= 1 && y <= 8);

    public static String borderToDraw(int y) {
        return switch (y) {
            case 1 -> " 1 ";
            case 2 -> " 2 ";
            case 3 -> " 3 ";
            case 4 -> " 4 ";
            case 5 -> " 5 ";
            case 6 -> " 6 ";
            case 7 -> " 7 ";
            case 8 -> " 8 ";

            default -> throw new IllegalStateException("Unexpected value: " + y);
        };
    }

    public static void printBoard(ChessBoard board) {
        StringBuilder toPrint = new StringBuilder();

        for (int j = 9; j > -1; j--) {
            for (int i = 0; i < 10; i++) {
                if (isHeaderPos.confirm(i, j))
                    toPrint.append("   a  b  c  d  e  f  g  h");

                else {
                    if (isBorderPos.confirm(i, j))
                        toPrint.append(borderToDraw(j));

                    if (isInsideBoard.confirm(i, j)) {
                        ChessPiece currentPiece = ChessBoard.getPieceOnCoords(board.getPieces(), i, j);

                        if (currentPiece != null)
                            toPrint.append(currentPiece.getSymbol());
                        else
                            toPrint.append(drawTile(i, j));
                    }
                }
            }

            toPrint.append('\n');
        }
        System.out.println(toPrint);
    }
}
