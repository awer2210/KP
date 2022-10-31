package model;

import java.util.ArrayList;

public class CheckersData {
    public static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;
    static int[][] board;

    public CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    public static void setUp(int row, int col, int val) {
        if(val == 0){
            board[row][col] = EMPTY;
        }
        else if(val == 1){
            board[row][col] = RED;
        }
        else if(val == 2){
            board[row][col] = RED_KING;
        }
        else if(val == 3){
            board[row][col] = BLACK;
        }
        else if(val == 4){
            board[row][col] = BLACK_KING;
        }
    }

    public void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3)
                        board[row][col] = BLACK;
                    else if (row > 4)
                        board[row][col] = RED;
                    else
                        board[row][col] = EMPTY;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    public int pieceAt(int row, int col) {
        return board[row][col];
    }

    public void makeMove(CheckersMove move) {
        makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;
        if (fromRow - toRow == 2 || fromRow - toRow == -2) {
            int jumpRow = (fromRow + toRow) / 2;
            int jumpCol = (fromCol + toCol) / 2;
            board[jumpRow][jumpCol] = EMPTY;
        }
        if (toRow == 0 && board[toRow][toCol] == RED)
            board[toRow][toCol] = RED_KING;
        if (toRow == 7 && board[toRow][toCol] == BLACK)
            board[toRow][toCol] = BLACK_KING;
    }

    public CheckersMove[] getLegalMoves(int player) {

        if (player != RED && player != BLACK)
            return null;

        int playerKing;
        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player || board[row][col] == playerKing) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moves.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moves.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moves.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moves.add(new CheckersMove(row, col, row - 2, col - 2));
                }
            }
        }
        if (moves.size() == 0) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player || board[row][col] == playerKing) {
                        if (canMove(player, row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    }
                }
            }
        }
        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    public CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        if (player != RED && player != BLACK)
            return null;
        int playerKing;
        if (player == RED)
            playerKing = RED_KING;
        else
            playerKing = BLACK_KING;
        ArrayList<CheckersMove> moves = new ArrayList<>();
        if (board[row][col] == player || board[row][col] == playerKing) {
            if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                moves.add(new CheckersMove(row, col, row + 2, col + 2));
            if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                moves.add(new CheckersMove(row, col, row - 2, col + 2));
            if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                moves.add(new CheckersMove(row, col, row + 2, col - 2));
            if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                moves.add(new CheckersMove(row, col, row - 2, col - 2));
        }
        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    public boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false;

        if (board[r3][c3] != EMPTY)
            return false;

        if (player == RED) {
            if (board[r1][c1] == RED && r3 > r1)
                return false;
            return board[r2][c2] == BLACK || board[r2][c2] == BLACK_KING;
        } else {
            if (board[r1][c1] == BLACK && r3 < r1)
                return false;
            return board[r2][c2] == RED || board[r2][c2] == RED_KING;
        }

    }

    public boolean canMove(int player, int r1, int c1, int r2, int c2) {

        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false;

        if (board[r2][c2] != EMPTY)
            return false;

        if (player == RED) {
            return board[r1][c1] != RED || r2 <= r1;
        } else {
            return board[r1][c1] != BLACK || r2 >= r1;
        }

    }

    public static class CheckersMove {
        public int fromRow;
        public int fromCol;
        public int toRow;
        public int toCol;

        public CheckersMove(int r1, int c1, int r2, int c2) {
            fromRow = r1;
            fromCol = c1;
            toRow = r2;
            toCol = c2;
        }

        public boolean isJump() {
            return (fromRow - toRow == 2 || fromRow - toRow == -2);
        }
    }
}




