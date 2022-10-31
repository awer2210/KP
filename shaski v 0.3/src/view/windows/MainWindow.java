package view.windows;

import view.panels.Checkers;
import io.FileSaver;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class MainWindow extends JFrame {
    private static final String title = "ABC";
    private static final String extension = "g4m4";
    private final Checkers content = new Checkers();
    public MainWindow(){
        super();
        var mainMenu = new MainMenu();
        mainMenu.window=this;
        changeTitle(null);
        setJMenuBar(mainMenu);
        setContentPane(content);
        pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( (screensize.width - getWidth())/2,
                (screensize.height - getHeight())/2 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable(false);
        setVisible(true);
    }
    public String save()
    {
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Сохранение файла");
        ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ch.setFileFilter(new FileNameExtensionFilter(title, extension));
        ch.setCurrentDirectory(new File(System.getProperty("user.dir")));
        ch.setSelectedFile(new File("Без имени." + extension));
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            return ch.getSelectedFile().getAbsolutePath();
        return null;
    }
    /*public void save() throws IOException {
        if(_curFile == null){
            if((_curFile = FileSaver.save(this)) != null) {
                var s = _curFile.split(Pattern.quote(System.getProperty("file.separator")));
                JOptionPane.showMessageDialog(this, "Файл " + s[s.length - 1] + " сохранен");
                changeTitle();
            }
            else return;
        }
        Checkers.save(_curFile);
    }*//*
    public void load(){
        String s = FileSaver.load(this);
        if (s != null) {
            _curFile = s;
            Checkers.loadContinue(_curFile);
            changeTitle();
        }
    }*/
    public String load() {
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Загрузка файла");
        ch.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ch.setFileFilter(new FileNameExtensionFilter(title, extension));
        ch.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            return ch.getSelectedFile().getAbsolutePath();
        return null;
    }

    public void resign(){
        Checkers.doResign();
    }
    public void newGame(){
        Checkers.NewGame();
    }
    public void changeTitle(String file) {
        if (file == null) setTitle("untitled - " + title);
        else {
            var s = file.split(Pattern.quote(System.getProperty("file.separator")));
            setTitle(s[s.length - 1].split("\\.")[0] + " - " + title);
        }
    }


    public class MainMenu extends JMenuBar {
        protected MainWindow window;
        public MainMenu(){

            super();
            add(createFileMenu());
        }

        private JMenu createFileMenu() {
            JMenu file = new JMenu("Файл");
            JMenuItem newGame = new JMenuItem("Новая игра");
            JMenuItem resign = new JMenuItem("сброс");
            JMenuItem load = new JMenuItem("Загрузить");
            JMenuItem save = new JMenuItem("Сохранить");

            file.add(newGame);
            file.add(resign);
            file.add(load);
            file.add(save);

            newGame.addActionListener(e -> newGame());
            newGame.setAccelerator(KeyStroke.getKeyStroke("control Z"));
            resign.addActionListener(e -> resign());
            resign.setAccelerator(KeyStroke.getKeyStroke("control X"));
            load.addActionListener(e -> FileSaver.load(window,content));
            load.setAccelerator(KeyStroke.getKeyStroke("control C"));
            save.addActionListener(e -> {
                try {
                    FileSaver.save(window,content);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            save.setAccelerator(KeyStroke.getKeyStroke("control V"));
            return file;
        }
    }
}
