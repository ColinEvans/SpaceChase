import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Colin on 2017-06-30.
 */
public class OptionsView extends JFrame implements Observer {

    private Model model;
    private int FPS;
    private int scrollSpeed;

    public OptionsView(Model model){
        //Frame options
        this.setTitle("Space Chase - In Game");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //text box options to be set in the game
        this.FPS = model.getFPS();
        this.scrollSpeed = model.getScrollSpeed();
        this.model = model;

        //add the panel, grid layout with 1 column and 6 rows
        JPanel framesPanel = new JPanel();
        JPanel scrollPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JPanel buttonsParent = new JPanel();
        HorizontalLine myLine = new HorizontalLine();


        //add all the widgets needed for the panel
        JLabel frames = new JLabel("FPS: ", JLabel.CENTER);
        JLabel scrollSpeed = new JLabel("Scroll Speed: ", JLabel.CENTER);
        JTextField framesFeild = new JTextField(1);
        framesFeild.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        framesFeild.setHorizontalAlignment(JTextField.CENTER);
        framesFeild.setText(String.valueOf(model.getFPS()));
        JTextField scrollFeild = new JTextField(1);
        scrollFeild.setText(String.valueOf(model.getScrollSpeed()));
        scrollFeild.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        scrollFeild.setHorizontalAlignment(JTextField.CENTER);
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        //panel for FPS
        framesPanel.setLayout(new GridLayout(1,2));
        frames.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        frames.setToolTipText("The refresh rate of gameplay!");
        framesPanel.add(frames);
        framesPanel.add(framesFeild);

        //panel for scroll speed
        scrollPanel.setLayout(new GridLayout(1,2));
        scrollSpeed.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        scrollSpeed.setToolTipText("The speed in which the game scrolls at!");
        scrollPanel.add(scrollSpeed);
        scrollPanel.add(scrollFeild);


        //controller for save button action listener here
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //for wider buttons  since box alignment is set to x can only set x axis
        saveButton.setPreferredSize(new Dimension((int) (saveButton.getPreferredSize().getWidth() * 2), 500));
        cancelButton.setPreferredSize(new Dimension((int) (cancelButton.getPreferredSize().getWidth() * 2), 500));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        //add the buttons to the parent
        buttonsParent.setLayout(new BoxLayout(buttonsParent, BoxLayout.X_AXIS));
        buttonsParent.add(Box.createHorizontalGlue());
        buttonsParent.add(buttonsPanel);
        buttonsParent.add(Box.createHorizontalGlue());

        //add to the parent container
        this.setLayout(new GridLayout(6, 1));
        this.add(framesPanel);
        this.add(scrollPanel);
        this.add(myLine);
        this.add(buttonsParent);


        //add controller
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //save to the model
                int framesData = Integer.parseInt(framesFeild.getText());
                int scrollData = Integer.parseInt(scrollFeild.getText());

                //change the data in the model if
                if (framesData != model.getFPS()){
                    model.setFPS(framesData);
                    model.notifyObservers();
                }
                if (scrollData != model.getScrollSpeed()){
                    model.setScrollSpeed(scrollData);
                    model.notifyObservers();
                }

                //go back to main page
                GamePlayView gamePlayView = new GamePlayView(model);
                setVisible(false);
                add(gamePlayView);
            }
        });

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                GamePlayView gamePlayView = new GamePlayView(model);
                setVisible(false);
                add(gamePlayView);
            }
        });


        setVisible(true);

    }


    public void update(Object obs) {
        if(obs instanceof LevelEditorModel){
            //redefine the model as a new version may need to be passed
            this.FPS = ((Model) obs).getFPS();
            this.scrollSpeed = ((Model) obs).getFPS();
            System.out.println("update changed");
        }
    }

}