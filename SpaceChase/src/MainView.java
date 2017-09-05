
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import javax.swing.*;
import java.io.File;

public class MainView extends JFrame implements Observer {
    private Model model;

    public MainView(Model model) {
        // Hook up this observer so that it will be notified when the model
        this.model = model;
        model.addObserver(this);

        // Basic Frame Options
        this.setTitle("Space Chase");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //all the view's panels
        JPanel titlePanel = new JPanel();
        JPanel mainControlPanel = new JPanel();
        JPanel mainGamePanel = new JPanel();
        JPanel spacer = new JPanel();
        JPanel middlePanel = new JPanel();
        JLabel gameTitle = new JLabel("SPACE CHASE");
        JButton startButton = new JButton("Start Game");
        JButton levelEditor = new JButton("Level Editor");

        //add the title to the panel and frame
        gameTitle.setFont(new Font ("SANS_SERIF", Font.PLAIN, 50));
        gameTitle.setToolTipText("SPACE CHASE!");
        titlePanel.add(gameTitle);

        //set up the box layout for the buttons
        mainGamePanel.setLayout(new BoxLayout(mainGamePanel, BoxLayout.X_AXIS));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelEditor.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension((int) (startButton.getPreferredSize().getWidth() * 2) ,500));
        levelEditor.setPreferredSize(new Dimension((int) (levelEditor.getPreferredSize().getWidth() * 2),500));
        mainGamePanel.add(startButton);
        mainGamePanel.add(levelEditor);

        //middle panel will be a gird layout with one row and 3 cols
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.add(Box.createHorizontalGlue());
        middlePanel.add(mainGamePanel);
        middlePanel.add(Box.createHorizontalGlue());

        //add the JPanels to set up the buttons
        mainControlPanel.setLayout(new GridLayout(3,1));
        mainControlPanel.add(spacer);
        mainControlPanel.add(titlePanel);
        mainControlPanel.add(middlePanel);

        model.setMainPage(mainControlPanel);
        model.notifyObservers();

        //add the nested panels to frame
        this.add(mainControlPanel);

        //the controller
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GamePlayView gamePlayView = new GamePlayView(model);
                mainControlPanel.setVisible(false);
                add(gamePlayView);
                setTitle("Space Chase - Main Game");
            }
        });

        levelEditor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String blockHeight;
                String blockWidth;

                //set the block height/width before main view is started
                JFrame worldSize = new JFrame("Set World Dimensions!");
                worldSize.setSize(450, 450);
                worldSize.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel container = new JPanel();
                container.setLayout(new GridLayout(4,1));

                //set the labels for choosing the world height / width
                JPanel labelContainer = new JPanel();
                labelContainer.setLayout(new GridLayout(4,1));

                //height text field
                JPanel heightParent = new JPanel();
                heightParent.setLayout(new GridLayout(1,2));
                JLabel worldHeight = new JLabel(" World Height (Blocks): ");
                worldHeight.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
                JTextField enterHeight = new JTextField(1);
                heightParent.add(worldHeight);
                heightParent.add(enterHeight);

                //width text field
                JPanel widthParent = new JPanel();
                widthParent.setLayout(new GridLayout(1,2));
                JLabel worldWidth = new JLabel(" World Width (Blocks): ");
                worldWidth.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
                JTextField enterWidth = new JTextField(1);
                widthParent.add(worldWidth);
                widthParent.add(enterWidth);

                labelContainer.add(heightParent);
                labelContainer.add(widthParent);

                //set a title
                JLabel title = new JLabel(" Enter Game Dimensions");
                title.setFont(new Font("SANS_SERIF", Font.PLAIN,25));

                //set a button
                JPanel buttonContainer = new JPanel();
                buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));
                buttonContainer.add(Box.createHorizontalGlue());
                JButton saveButton = new JButton("Save Choices");
                saveButton.setPreferredSize(new Dimension((int) saveButton.getPreferredSize().getWidth() * 2, 500));
                saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                saveButton.setFont(new Font("SANS_SERIF", Font.PLAIN,15));
                buttonContainer.add(saveButton);
                JButton openButton = new JButton("Open From File");
                openButton.setPreferredSize(new Dimension((int) saveButton.getPreferredSize().getWidth() * 2, 500));
                openButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                openButton.setFont(new Font("SANS_SERIF", Font.PLAIN,15));
                buttonContainer.add(openButton);
                buttonContainer.add(Box.createHorizontalGlue());

                openButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                    myFileChooser fileChooser = new myFileChooser();

                    //remove current layout
                    worldSize.remove(container);

                    //add the file selector to the frame
                    worldSize.add(fileChooser);
                    worldSize.setVisible(true);

                    //window listener updates the level model on close to be the right file
                    worldSize.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            //this has to select the correct file and load it to the editor
                            //ask dan if using absolute path will work it should
                            File filePath = fileChooser.getFile();
                            new LevelEditorView(0,0, filePath);

                        }
                    });
                    }
                });

                saveButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String enteredHeight = enterHeight.getText();
                        String enteredWidth = enterWidth.getText();
                        File myFile = new File("level");
                        try{
                            int blockHeight = Integer.parseInt(enteredHeight);
                            int blockWidth = Integer.parseInt(enteredWidth);
                            LevelEditorView level = new LevelEditorView(blockHeight, blockWidth, myFile);
                            worldSize.dispose();
                        }catch(NumberFormatException err){
                            JOptionPane.showMessageDialog(null,"Please Enter Valid Integers" );
                        }
                    }
                });

                container.add(title);
                container.add(labelContainer);
                container.add(buttonContainer);

                worldSize.add(container);
                worldSize.setVisible(true);
            }
        });

        setVisible(true);
    }

    public void update(Object obs) {
        if(obs instanceof Model){
            //redefine the model as a new version may need to be passed
           this.model = ((Model) obs);
        }
    }
}
