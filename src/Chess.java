import Classes.ChessBoard;
import Classes.ChessGraphics;
import Classes.ChessPieces.ChessPiece;
import Classes.ChessPieces.King;
import Classes.ChessPieces.Pawn;
import Classes.SaveSystem.ChessReader;
import Classes.SaveSystem.ChessSaver;
import Interfaces.CastlingCapable;
import Interfaces.StringExtractor;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Chess {
    boolean whiteTurn = true;
    boolean draw = false;
    int oldX;
    int oldY;
    int turnCount;
    int pawn2TileTurnCount;
    int pawn2TileX;
    int pawn2TileY;
    Scanner scan = new Scanner(System.in);
    String userInput;
    ChessBoard board;
    ChessPiece selectedPiece;

    public static void main(String[] args) throws IOException {
        System.setOut(
                new PrintStream(
                        new FileOutputStream(FileDescriptor.out),
                        true,
                        StandardCharsets.UTF_8
                )
        );

        Scanner scan = new Scanner(System.in);
        String userInput;
        ChessBoard board = null;

        System.out.println("Type \"new game\" to start a new game or \"load game\" to load a previously saved game");
        userInput = scan.nextLine();

        switch (userInput) {
            case "new game" -> board = new ChessBoard();
            case "load game" -> {
                System.out.println("Name of save file: ");
                userInput = scan.nextLine()+".bin";

                board = new ChessBoard(ChessReader.boardReader(userInput));
            }
        }

        new Chess(board).playGame();
    }

    public Chess(ChessBoard board) {
        this.board = board;
    }

    public void kingCheckStatus() {
        if (whiteTurn){
            if (ChessBoard.getCheckingPiece(board.getPieces(), ChessBoard.getKing(board.getPieces(), true)) != null)
                System.out.println("White King in Check!");
        }

        else {
            if (ChessBoard.getCheckingPiece(board.getPieces(), ChessBoard.getKing(board.getPieces(), false)) != null)
                System.out.println("Black King in Check!");
        }
    }

    public boolean isCheckEscapable(ArrayList<ChessPiece> pieces, ChessPiece allyPiece) {
        ArrayList<ChessPiece> piecesClone = ChessBoard.clonePieces(pieces);
        ChessPiece allyClone = ChessBoard.getPieceOnCoords(piecesClone, allyPiece.getX(), allyPiece.getY());
        King kingClone = ChessBoard.getKing(piecesClone, whiteTurn);
        assert allyClone != null;

        for (int y = 1; y < 9; y++){
            for (int x = 1; x < 9; x++){

                allyClone.move(piecesClone, x, y);

                if (ChessBoard.getCheckingPieceList(piecesClone, kingClone).isEmpty())
                    return true;

                allyClone.setX(allyPiece.getX());
                allyClone.setY(allyPiece.getY());
            }
        }
        return false;
    }

    public boolean isNotCheckMate(ArrayList<ChessPiece> pieces) {
        for (ChessPiece allyPiece : pieces) {
            if (allyPiece.isWhite() == whiteTurn && isCheckEscapable(pieces, allyPiece))
                return true;
        }

        ChessGraphics.printBoard(board);
        if (whiteTurn)
            System.out.println("Black wins by checkmate!");
        else
            System.out.println("White wins by checkmate!");

        return false;
    }

    public void turnAnnouncer() {
        if (whiteTurn)
            System.out.println("White's turn");
        else
            System.out.println("Black's turn");
    }

    StringExtractor extractX = (String userInput) -> userInput.length() == 2 ? userInput.toLowerCase().charAt(0) - 96 : -1;

    StringExtractor extractY = (String userInput) -> userInput.length() == 2 ? Character.getNumericValue(userInput.charAt(1)) : -1;

    public void pieceQuery() {
        System.out.println("Which piece: ");
        userInput = scan.nextLine();

        selectedPiece = ChessBoard.getPieceOnCoords(
                board.getPieces(),
                extractX.extract(userInput),
                extractY.extract(userInput)
        );
    }

    public boolean isEligiblePiece(ChessPiece p1) {
        return p1 != null && p1.isWhite() == whiteTurn;
    }

    public void saveOldCoords() {
        oldX = selectedPiece.getX();
        oldY = selectedPiece.getY();
    }

    public boolean isSafeMove() {
        ArrayList<ChessPiece> piecesClone = ChessBoard.clonePieces(board.getPieces());
        ChessPiece allyClone = ChessBoard.getPieceOnCoords(piecesClone, selectedPiece.getX(), selectedPiece.getY());

        System.out.println("Where: ");
        userInput = scan.nextLine();

        assert allyClone != null;
        allyClone.move(piecesClone,
                extractX.extract(userInput),
                extractY.extract(userInput)
        );

        return (ChessBoard.getCheckingPiece(piecesClone, ChessBoard.getKing(piecesClone, whiteTurn)) == null);
    }

    public void moveQuery() {
        if (isSafeMove())
            selectedPiece.move(
                    board.getPieces(),
                    extractX.extract(userInput),
                    extractY.extract(userInput)
            );
    }

    public boolean isMoveSuccess(ChessPiece p1) {
        return p1.getX() != oldX || p1.getY() != oldY;
    }

    public boolean isEnPassantEligible(ChessPiece p1) {
        return isEligiblePiece(p1) && (p1.getSymbol() == '♙' || p1.getSymbol() == '♟');
    }

    public void enPassantQuery() {
        if (isSafeMove())
            ((Pawn)selectedPiece).enPassant(
                    board.getPieces(),
                    turnCount,
                    pawn2TileTurnCount,
                    pawn2TileX, pawn2TileY,
                    extractX.extract(userInput),
                    extractY.extract(userInput)
            );
    }

    public boolean isCastlingEligible(ChessPiece p1) {
        return isEligiblePiece(p1) && ((p1.getSymbol() == '♔' || p1.getSymbol() == '♚') || (p1.getSymbol() == '♖' || p1.getSymbol() == '♜'));
    }

    public void castlingQuery() {
        System.out.println("Swap with: ");
        userInput = scan.nextLine();

        ((CastlingCapable)selectedPiece).castling(
                board.getPieces(),
                extractX.extract(userInput),
                extractY.extract(userInput)
        );
    }

    public void saveLast2TilePawnState(ChessPiece p1) {
        pawn2TileTurnCount = turnCount;
        pawn2TileX = p1.getX();
        pawn2TileY = p1.getY();
    }

    public void pawnAfterMoveAct(ArrayList<ChessPiece> board, ChessPiece p1) {
        if (p1.getSymbol() == '♙' || p1.getSymbol() == '♟') {
            if (p1.getY() == oldY + 2)
                saveLast2TilePawnState(p1);

            else if (p1.getY() == 8 || p1.getY() == 1)
                Pawn.promote(board, p1);
        }
    }

    public void turnEnd() {
        whiteTurn = !whiteTurn;
        turnCount++;
    }

    public void drawVote() {
        String userInput = "";

        while(!userInput.equals("draw") && !userInput.equals("refuse")) {
            System.out.println(
                    "Draw vote: 1/2\n" +
                            "Type \"draw\" to confirm or \"refuse\" to continue playing"
            );
            userInput = scan.nextLine();
            if (userInput.equals("draw")) {
                System.out.println("Draw!");
                draw = true;
                turnEnd();
            }
        }
    }

    public void saveGame() throws IOException {
        System.out.println("File name: ");
        String userInput;

        userInput = scan.nextLine()+".bin";
        ChessSaver.boardSaver(board, userInput);
    }

    public void loadGame() throws IOException {
        System.out.println("File name: ");
        String userInput;

        userInput = scan.nextLine()+".bin";
        board = new ChessBoard (ChessReader.boardReader(userInput));
        ChessGraphics.printBoard(board);
        turnAnnouncer();
    }

    public void playerAction() throws IOException {
        System.out.println("Your action: ");
        userInput = scan.nextLine();

        switch (userInput) {

            case "move" -> {
                pieceQuery();

                if (isEligiblePiece(selectedPiece)) {
                    saveOldCoords();
                    moveQuery();

                    if (isMoveSuccess(selectedPiece)) {
                        pawnAfterMoveAct(board.getPieces(), selectedPiece);
                        turnEnd();
                    }
                }
            }

            case "en passant" -> {
                pieceQuery();

                if (isEnPassantEligible(selectedPiece)) {
                    saveOldCoords();
                    enPassantQuery();

                    if (isMoveSuccess(selectedPiece)) {
                        turnEnd();
                    }
                }
            }

            case "castling" -> {
                pieceQuery();

                if (isCastlingEligible(selectedPiece)) {
                    saveOldCoords();
                    castlingQuery();

                    if (isMoveSuccess(selectedPiece)) {
                        turnEnd();
                    }
                }
            }

            case "draw" -> drawVote();

            case "save" -> saveGame();

            case "load" -> loadGame();
        }
    }

    public void playGame() {
        while(!draw && isNotCheckMate(board.getPieces())) {
            ChessGraphics.printBoard(board);
            kingCheckStatus();
            turnAnnouncer();

            while(whiteTurn) {
                try {
                    playerAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (!draw && isNotCheckMate(board.getPieces())) {
                ChessGraphics.printBoard(board);
                kingCheckStatus();
                turnAnnouncer();

                while(!whiteTurn) {
                    try {
                        playerAction();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
