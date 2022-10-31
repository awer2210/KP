package view.panels;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Checkers extends JPanel {
    private JLabel message;
    private static Board board;

    public Checkers() {
        setLayout(null);
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(159, 121, 238));
        board = new Board();
        board.base = this;
        message = new JLabel("", JLabel.CENTER);
        message.setFont(new Font("", Font.BOLD, 14));
        message.setForeground(Color.green);
        board.doNewGame();
        add(board);
        add(message);
        board.setBounds(20, 20, 324, 324);
        message.setBounds(80, 350, 200, 100);
    }
    public static void NewGame()
    {
        board.doNewGame();
    }
    public void save(String s) throws IOException {
    board.doSave(s);
}
    public void loadContinue(String s)
    {
        board.doContinue(s);
    }
    public static void doResign()
    {
        board.doResign();
    }
    public void printText(String s) {
        message.setText(s);
    }
}