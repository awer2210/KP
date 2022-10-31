
import view.windows.MainWindow;

import java.awt.*;

public class Main {
    private static void viewTest(){
        EventQueue.invokeLater(() -> {
            var ex = new MainWindow();
            ex.setLocationRelativeTo(null);
            ex.setVisible(true);
        });
    }
    private static void modelTest(){

    }
    public static void main(String[] args) {
        viewTest();
        modelTest();
    }
}