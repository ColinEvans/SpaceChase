/**
 * Created by Colin on 2017-06-17.
 */

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;

public class GamePlayView extends JPanel implements Observer{

    private Model model;

    public GamePlayView(Model model) {
        this.model = model;
        model.addObserver(this);
        model.notifyObservers();

        //add a button to go back to the main view
        JButton backButton = new JButton("Back");

        //all the views panels
        JPanel helpOptionsParent = new JPanel(); //used for containing the help options
        JPanel gameOptionsParent = new JPanel();
        JPanel titlePanel = new JPanel(); //view title
        JPanel spacer = new JPanel(); //this will be the bread crumbs
        JPanel gameOptionsPanel = new JPanel(); //playgame buttons
        JPanel helpOptionsPanel = new JPanel(); //select options buttons

        //all the views widgets
        JLabel gameTitle = new JLabel("Main Game");
        JButton gamePlayButton = new JButton("Play Level"); //plays the default level
        JButton selectLevelButton = new JButton("Load Level"); //allows the user to load a level
        JButton optionsButton = new JButton("Options");
        JButton helpButton = new JButton("Help");

        //add the title to the panel and frame
        gameTitle.setFont(new Font("SANS_SERIF", Font.PLAIN, 50));
        gameTitle.setToolTipText("SPACE CHASE!");
        titlePanel.add(gameTitle);

        //set up x-axis box layout for gameplay options
        gameOptionsPanel.setLayout(new BoxLayout(gameOptionsPanel, BoxLayout.X_AXIS));
        gamePlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        gamePlayButton.setPreferredSize(new Dimension((int) (gamePlayButton.getPreferredSize().getWidth() * 2), 500));
        selectLevelButton.setPreferredSize(new Dimension((int) (selectLevelButton.getPreferredSize().getWidth() * 2), 500));
        gameOptionsPanel.add(gamePlayButton);
        gameOptionsPanel.add(selectLevelButton);

        //gameplay buttons parent
        gameOptionsParent.setLayout(new BoxLayout(gameOptionsParent, BoxLayout.X_AXIS));
        gameOptionsParent.add(Box.createHorizontalGlue());
        gameOptionsParent.add(gameOptionsPanel);
        gameOptionsParent.add(Box.createHorizontalGlue());

        //set up y-axis box layout for the help buttons
        helpOptionsPanel.setLayout(new BoxLayout(helpOptionsPanel, BoxLayout.Y_AXIS));
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setPreferredSize(new Dimension(500, (int) (helpButton.getPreferredSize().getHeight() * 2 )));
        helpButton.setPreferredSize(new Dimension(500, (int) (helpButton.getPreferredSize().getHeight() * 2 )));
        backButton.setPreferredSize(new Dimension(500, (int) (helpButton.getPreferredSize().getHeight() * 2 )));
        helpOptionsPanel.add(optionsButton);
        helpOptionsPanel.add(helpButton);
        helpOptionsPanel.add(backButton);

        //help buttons parent
        helpOptionsParent.setLayout(new BoxLayout(helpOptionsParent, BoxLayout.X_AXIS));
        helpOptionsParent.add(Box.createHorizontalGlue());
        helpOptionsParent.add(helpOptionsPanel);
        helpOptionsParent.add(Box.createHorizontalGlue());

        //add the JPanels to set up the buttons
        this.setLayout(new GridLayout(4, 1));
        this.add(spacer);
        this.add(titlePanel);
        this.add(gameOptionsParent);
        this.add(helpOptionsParent);


        //go back to main page
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel showMain = model.getMainPage();
                showMain.setVisible(true);
                setVisible(false);
            }
        });

        //add events for myButton
       gamePlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Game newGame = new Game(model);
            }
        });

       //bring up options screen
       optionsButton.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               OptionsView optionsView = new OptionsView(model);
           }
       });

       //bring up File chooser
        selectLevelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                myFileChooser fileChooser = new myFileChooser();
                //create a new FileChooser Window
                JFrame levelSelect = new JFrame("Select a level!");
                levelSelect.setSize(800, 600);
                levelSelect.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                //add the file selector to the frame
                levelSelect.add(fileChooser);

                levelSelect.setVisible(true);

                //grab the file on close
                levelSelect.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        model.setLevel(fileChooser.getFile());
                        model.notifyObservers();
                    }
                });

            }
        });

        //bring up options screen
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HelpMenu helpWindow = new HelpMenu();
            }
        });


        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object obs) {
        if (obs instanceof Model) {
            //redefine the model as a new version may need to be passed
            this.model = ((Model) obs);
            System.out.println("update changed");
        }
    }

}