import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Colin on 2017-07-24.
 */

public class LevelEditorMenu extends JMenuBar implements Observer {
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem cutMenuItem;

    private LevelEditorModel model;

    public  LevelEditorMenu(LevelEditorModel model) {
        this.model = model;
        this.model.addObserver(this);
        this.model.notifyObsevers();

        JMenu editMenu = new JMenu("Edit");
        this.add(editMenu);

        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(pasteMenuItem);

        //controller
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.undo();
            }
        });

        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.redo();
            }
        });

        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSelectedBlock() != null) {
                    model.doCopy();
                    model.notifyObsevers();
                }
            }
        });

        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSelectedBlock() != null) {
                    model.doCut();
                    model.notifyObsevers();
                }
            }
        });

        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getSelectedBlock() != null) {
                    model.doPaste();
                    model.notifyObsevers();
                }
            }
        });

    }


    public void update(Object obs) {
        if (obs instanceof LevelEditorModel){
            System.out.println("LevelEditorView - Menu Notified");
            this.model = (LevelEditorModel) obs;
        }
    }

}
