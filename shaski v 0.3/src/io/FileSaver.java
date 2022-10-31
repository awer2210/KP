package io;
import view.panels.Checkers;
import view.windows.MainWindow;

import java.io.IOException;

public class FileSaver {
    private static String curFile ;


    public static void save(MainWindow g, Checkers p) throws IOException {
        if(curFile == null) {
            if((curFile = g.save()) != null) {
                g.changeTitle(curFile);
            }
            else return;
        }
        p.save(curFile);
    }

    public static void load(MainWindow g, Checkers p){
        String s = g.load();
        if (s != null) {
            curFile = s;
            p.loadContinue(curFile);
            g.changeTitle(curFile);
        }
    }
}