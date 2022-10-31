package view.panels;
import model.CheckersData;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class Board extends JPanel implements MouseListener {
    public Checkers base;
    CheckersData board;
    boolean gameInProgress;
    int currentPlayer;
    int selectedRow, selectedCol;
    CheckersData.CheckersMove[] legalMoves;

    public Board() {
        setBackground(Color.BLACK);
        addMouseListener(this);
        board = new CheckersData();
        //doNewGame();
    }
    void Progress(int[] a) {
        int g = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2)
                    switch (board.pieceAt(row, col)) {
                        case CheckersData.EMPTY:
                            a[g++] = 0;
                            break;
                        case CheckersData.RED:
                            a[g++] = 1;
                            break;
                        case CheckersData.BLACK:
                            a[g++] = 3;
                            break;
                        case CheckersData.RED_KING:
                            a[g++] = 2;
                            break;
                        case CheckersData.BLACK_KING:
                            a[g++] = 4;
                            break;
                    }
            }
        }
        a[32] = currentPlayer;
    }
    void doSave(String path) throws IOException {
        int[] a;
        a = new int[33];
        Progress(a);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            for (int n = 0; n < 33; n++) {
                out.writeInt(a[n]);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    void doContinue(String path) {
        //board = new CheckersData();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            int[] number = new int[33];
            for (int i = 0; i < 32; i++) {
                number[i] = in.readInt();
            }
            int g = 0;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2) {
                        switch (number[g++]) {
                            case 0:
                                CheckersData.setUp(row, col, 0);
                                break;
                            case 1:
                                CheckersData.setUp(row, col, 1);
                                break;
                            case 2:
                                CheckersData.setUp(row, col, 2);
                                break;
                            case 3:
                                CheckersData.setUp(row, col, 3);
                                break;
                            case 4:
                                CheckersData.setUp(row, col, 4);
                                break;
                        }
                    }
                }
            }
            if (number[32] == 0) {
                currentPlayer = CheckersData.RED;
                legalMoves = board.getLegalMoves(CheckersData.RED);
                selectedRow = -1;
                base.printText("Red:  Make your move.");
            }else if(number[32] == 1) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(CheckersData.BLACK);
                selectedRow = -1;
                base.printText("Black:  Make your move.");
            }
            gameInProgress = true;
            repaint();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doNewGame() {
        if (gameInProgress == true) {
            //message.setText("Finish the current game first!");
            base.printText("Finish the current game first!");
            return;
        }
        board.setUpGame();
        currentPlayer = CheckersData.RED;
        legalMoves = board.getLegalMoves(CheckersData.RED);
        selectedRow = -1;
        base.printText("Red:  Make your move.");
        gameInProgress = true;
        repaint();
    }

    void doResign() {
        if (gameInProgress == false) {  // Should be impossible.
            base.printText("There is no game in progress!");
            return;
        }
        if (currentPlayer == CheckersData.RED)
            gameOver("RED resigns.  BLACK wins.");
        else
            gameOver("BLACK resigns.  RED wins.");
    }

    void gameOver(String str) {
        base.printText(str);
        gameInProgress = false;
    }

    void doClickSquare(int row, int col) {
        for (int i = 0; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                selectedRow = row;
                selectedCol = col;
                if (currentPlayer == CheckersData.RED)
                    base.printText("RED:  Make your move.");
                else
                    base.printText("BLACK:  Make your move.");
                repaint();
                return;
            }
        if (selectedRow < 0) {
            base.printText("Click the piece you want to move.");
            return;
        }
        for (var legalMove : legalMoves)
            if (legalMove.fromRow == selectedRow && legalMove.fromCol == selectedCol
                    && legalMove.toRow == row && legalMove.toCol == col) {
                doMakeMove(legalMove);
                return;
            }
        base.printText("Click the square you want to move to.");
    }

    public void doMakeMove(CheckersData.CheckersMove move) {
        board.makeMove(move);
        if (move.isJump()) {
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) {
                if (currentPlayer == CheckersData.RED)
                    base.printText("RED:  You must continue jumping.");
                else
                    base.printText("BLACK:  You must continue jumping.");
                selectedRow = move.toRow;  // Since only one piece can be moved, select it.
                selectedCol = move.toCol;
                repaint();
                return;
            }
        }
        if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("BLACK has no moves.  RED wins.");
            else if (legalMoves[0].isJump())
                base.printText("BLACK:  Make your move.  You must jump.");
            else
                base.printText("BLACK:  Make your move.");
        } else {
            currentPlayer = CheckersData.RED;
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null)
                gameOver("RED has no moves.  BLACK wins.");
            else if (legalMoves[0].isJump())
                base.printText("RED:  Make your move.  You must jump.");
            else
                base.printText("RED:  Make your move.");
        }
        selectedRow = -1;

        if (legalMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameStartSquare = false;
                    break;
                }
            if (sameStartSquare) {
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {

        g.setColor(Color.black);
        g.drawRect(0, 0, getSize().width + 3, getSize().height +3 );
         g.drawRect(1, 1, getSize().width + 3, getSize().height + 3);
        g.drawRect(2, 2, getSize().width + 3, getSize().height + 3);
        g.drawRect(3, 3, getSize().width + 3, getSize().height + 3);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2)
                    g.setColor(Color.LIGHT_GRAY);
                else
                    g.setColor(Color.GRAY);
                g.fillRect(4 + col * 40, 4 + row * 40, 40, 40);
                switch (board.pieceAt(row, col)) {
                    case CheckersData.RED:
                        g.setColor(Color.RED);
                        g.fillOval(8 + col * 40, 8 + row * 40, 30, 30);
                        break;
                    case CheckersData.BLACK:
                        g.setColor(Color.BLACK);
                        g.fillOval(8 + col * 40, 8 + row * 40, 30, 30);
                        break;
                    case CheckersData.RED_KING:
                        g.setColor(Color.RED);
                        g.fillOval(8 + col * 40, 8 + row * 40, 30, 30);
                        g.setColor(Color.WHITE);
                        g.drawString("Д", 20 + col * 40, 28 + row * 40);
                        break;
                    case CheckersData.BLACK_KING:
                        g.setColor(Color.BLACK);
                        g.fillOval(8 + col * 40, 8 + row * 40, 30, 30);
                        g.setColor(Color.WHITE);
                        g.drawString("Д", 20 + col * 40, 28 + row * 40);
                        break;
                }
            }
        }
        if (gameInProgress) {
            g.setColor(Color.cyan);
            for (int i = 0; i < legalMoves.length; i++) {
                g.drawRect(4 + legalMoves[i].fromCol * 40, 4 + legalMoves[i].fromRow * 40, 40, 40);
                g.drawRect(6 + legalMoves[i].fromCol * 40, 6 + legalMoves[i].fromRow * 40, 36, 36);
            }
            if (selectedRow >= 0) {
                g.setColor(Color.white);
                g.drawRect(4 + selectedCol * 40, 4 + selectedRow * 40, 40, 40);
                g.drawRect(6 + selectedCol * 40, 6 + selectedRow * 40, 36, 36);
                g.setColor(Color.green);
                for (int i = 0; i < legalMoves.length; i++) {
                    if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                        g.drawRect(4 + legalMoves[i].toCol * 40, 4 + legalMoves[i].toRow * 40, 40, 40);
                        g.drawRect(6 + legalMoves[i].toCol * 40, 6 + legalMoves[i].toRow * 40, 36, 36);
                    }
                }
            }
        }

    }

    public void mousePressed(MouseEvent evt) {
        if (gameInProgress == false)
            base.printText("Click \"New Game\" to start a new game.");
        else {
            int col = (evt.getX() - 2) / 40;
            int row = (evt.getY() - 2) / 40;
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
                doClickSquare(row, col);
        }
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }
}