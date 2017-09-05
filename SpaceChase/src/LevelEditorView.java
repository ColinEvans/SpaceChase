import javax.swing.*;
import javax.swing.Timer;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.util.List;
import java.io.File;

/**
 * Created by Colin on 2017-07-20.
 */

public class LevelEditorView extends JFrame implements Observer{
    private LevelEditorModel model;
    private JPanel container;
    private createBlock createPanel;
    private myCanvas canvas;
    private JScrollPane scroll;
    private File loadLevel;

    public LevelEditorView(int blockHeight, int blockWidth,File loadLevel) {
        //make a model and add the frame
        this.loadLevel = loadLevel;
        this.model = new LevelEditorModel(blockHeight, blockWidth, loadLevel);
        model.addObserver(this);
        model.notifyObsevers();

        //set the menu
        LevelEditorMenu menu = new LevelEditorMenu(this.model);
        this.setJMenuBar(menu);

        //set frame properties
        this.setSize(1200, 1200);
        this.setTitle("Space Chase - Level Editor!");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.container = new JPanel();
        this.container.setLayout(new GridLayout(1,2));

        //create the left panel
        this.createPanel = new createBlock(model);
        container.add(createPanel);

        //create the canvas on the right
        this.canvas = new myCanvas(model);
        this.scroll = new JScrollPane(canvas);
        container.add(scroll);

        this.add(container);
        this.setVisible(true);


    }

    //update the view with new model information
    public void update(Object obs) {
        if (obs instanceof LevelEditorModel){
            System.out.println("LevelEditorView Notified");
        }
    }

}

//where the painting takes place
class myCanvas extends JPanel implements Observer{
    private LevelEditorModel model;
    private List<BlockView> blocks;
    private UndoManager undo;
    private Timer timer;

    public myCanvas(LevelEditorModel model){
        super();

        this.model = model;
        this.model.addObserver(this);
        this.model.notifyObsevers();
        this.model.resetBlockSize(getHeight());


        this.undo = new UndoManager();

        this.setPreferredSize(new Dimension(this.model.getBlockWidth() * this.model.getBlockSize(),
                this.model.getBlockHeight() * this.model.getBlockSize()));


        this.timer = new Timer(1000 / 30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.resetBlockSize(getHeight());
                setPreferredSize(new Dimension(model.getBlockWidth() * model.getBlockSize(),
                        model.getBlockHeight() * model.getBlockSize()));
                repaint();
            }
        });

        this.timer.start();


        MouseAdapter myAdapter = (new MouseAdapter() {
            Point startPos;
            int x1;
            int x2;
            int y1;
            int y2;

            @Override
            public void mousePressed(MouseEvent e) {
                startPos = e.getPoint();

                for(BlockView b : model.returnBlocks()){
                    Rectangle box = b.createBoundingBox();
                    int xPos = e.getX();
                    int yPos = e.getY();
                    Point mousePoint = new Point(xPos,yPos);
                    if (box.contains(mousePoint)){
                        //set init values
                        x1 = b.getx1();
                        x2 = b.getx2();
                        y1 = b.gety1();
                        y2 = b.gety2();

                        //create undo / redo action
                        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                            //redo the undone method
                            public void redo() throws CannotRedoException {
                                super.redo();
                                b.toggleSelected();
                                if(b.getSelected()){
                                    model.setSelectedBlock(b);
                                    model.notifyObsevers();
                                }else{
                                    model.removeSelectedBlock();
                                    model.notifyObsevers();
                                }
                            }

                            public void undo() throws CannotUndoException {
                                super.undo();
                                b.toggleSelected();
                                if(b.getSelected()){
                                    model.setSelectedBlock(b);
                                    model.notifyObsevers();
                                }else{
                                    model.removeSelectedBlock();
                                    model.notifyObsevers();
                                }
                            }
                        };

                        model.addToEdit(undoableEdit);
                        b.toggleSelected();
                        if(b.getSelected()){
                            model.setSelectedBlock(b);
                            model.notifyObsevers();
                        }else{
                            model.removeSelectedBlock();
                            model.notifyObsevers();
                        }
                    }
                }
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                BlockView b = model.getSelectedBlock();
                if( b != null){
                    Rectangle box = b.createBoundingBox();
                    int blockHeight = model.getBlockHeight();
                    int blockSize = getHeight()/blockHeight;

                    int newX = e.getX() / blockSize;
                    int newY = e.getY() / blockSize;

                    int width = b.getx2() - b.getx1();
                    int height = b.gety2() - b.gety1();

                    /* add undo and redo for moving the block
                      undo and redo works for how ever many drags made
                      could take many drags to show movement and therefore
                      many undos to make the block go back / forward */
                    UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                        //redo the undone method
                        public void redo() throws CannotRedoException {
                            super.redo();
                            b.setNetX1(newX);
                            b.setNetY1(newY);
                            b.setNetX2(newX + width);
                            b.setNetY2(newY + height);
                        }

                        public void undo() throws CannotUndoException {
                            super.undo();
                            b.setNetX1(x1);
                            b.setNetY1(y1);
                            b.setNetX2(x2);
                            b.setNetY2(y2);
                            b.recalculateBlocks();
                        }
                    };
                    model.addToEdit(undoableEdit);
                    b.setNetX1(newX);
                    b.setNetY1(newY);
                    b.setNetX2(newX + width);
                    b.setNetY2(newY + height);
                }

            }
        });

        this.addMouseListener(myAdapter);
        this.addMouseMotionListener(myAdapter);


    }


    @Override
    protected void paintComponent(Graphics g){

       for(BlockView b : model.returnBlocks()) {
            b.setNewSize(this.getHeight());
            b.recalculateBlocks();
            b.paint(g);
        }
    }


    public void stopTimer(){
        this.timer.stop();
    }

    // Update with data from the model.
    public void update(Object obs) {
        if(obs instanceof LevelEditorModel){
            System.out.println("LevelEditorView - Canvas Notified!");
            this.blocks = ((LevelEditorModel) obs).returnBlocks();
        }

    }



}

//button control area that creates a block onto the view
class createBlock extends JPanel implements Observer{
    //block height for creating a block
    private LevelEditorModel model;

    //Panels
    private JPanel container;
    private JPanel overallHeightContainer;
    //height and height 2
    private JPanel heightParent;
    private JLabel height;
    private JTextField enterHeight;
    private JPanel heightParent2;
    private JLabel height2;
    private JTextField enterHeight2;
    //the width and width2
    private JPanel widthParent;
    private JLabel width;
    private JTextField enterWidth;
    private JPanel widthParent2;
    private JLabel width2;
    private JTextField enterWidth2;
    //the buttons
    private JPanel buttonPanel;
    private JButton createButton;
    private JButton deleteButton;
    private JButton saveButton;


    public createBlock(LevelEditorModel model) {
        this.model = model;
        model.addObserver(this);
        model.notifyObsevers();

        //set parent container Layouts
        this.container = new JPanel();
        this.container.setLayout(new GridLayout(6, 1));
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.Y_AXIS));
        this.overallHeightContainer = new JPanel();
        this.overallHeightContainer.setLayout(new GridLayout(5,1));
        this.heightParent = new JPanel();
        this.heightParent.setLayout(new GridLayout(1,2));
        this.widthParent = new JPanel();
        this.widthParent.setLayout(new GridLayout(1,2));
        this.heightParent2 = new JPanel();
        this.heightParent2.setLayout(new GridLayout(1,2));
        this.widthParent2 = new JPanel();
        this.widthParent2.setLayout(new GridLayout(1,2));

        //set labels
        //height
        this.height = new JLabel("X1: ");
        this.height.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        this.height.setToolTipText("the obstacle's height in blocks");
        //height2
        this.height2 = new JLabel("X2: ");
        this.height2.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        this.height2.setToolTipText("the obstacle's height in blocks");
        //width
        this.width = new JLabel("Y1: ");
        this.width.setToolTipText("the obstacle's width in blocks");
        this.width.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        //width2
        this.width2 = new JLabel("Y2: ");
        this.width2.setToolTipText("the obstacle's width in blocks");
        this.width2.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        //set textFeilds
        this.enterHeight = new JTextField(1);
        this.enterHeight.setPreferredSize(new Dimension(150,50));
        this.enterHeight2 = new JTextField(1);
        this.enterHeight2.setPreferredSize(new Dimension(150,50));
        this.enterWidth = new JTextField(1);
        this.enterWidth.setPreferredSize(new Dimension(150,50));
        this.enterWidth2 = new JTextField(1);
        this.enterWidth2.setPreferredSize(new Dimension(150,50));

        //set buttons
        this.createButton = new JButton("Create Block");
        this.deleteButton = new JButton("Delete Block");
        this.saveButton = new JButton("Save Level");


        //add components to the parent panels
        heightParent.add(height);
        heightParent.add(enterHeight);
        overallHeightContainer.add(heightParent);
        widthParent.add(width);
        widthParent.add(enterWidth);
        overallHeightContainer.add(widthParent);
        heightParent2.add(height2);
        heightParent2.add(enterHeight2);
        overallHeightContainer.add(heightParent2);
        widthParent2.add(width2);
        widthParent2.add(enterWidth2);
        overallHeightContainer.add(widthParent2);
        overallHeightContainer.add(new JPanel());

        //add buttons to the panels
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(500, (int) createButton.getPreferredSize().getHeight() * 2));
        createButton.setPreferredSize(new Dimension(500, (int) createButton.getPreferredSize().getHeight() * 2));
        deleteButton.setPreferredSize(new Dimension(500, (int) deleteButton.getPreferredSize().getHeight() * 2));
        buttonPanel.add(createButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        //the overall container
        container.add(overallHeightContainer);
        //the block will be created here which then can be dragged and placed in the world
        container.add(new JPanel());
        container.add(buttonPanel);

        //add to the panel class
        this.add(container);

        this.setVisible(true);

        //set controller for the buttons
        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Creating Block");
                String height = enterHeight.getText();
                String height2 = enterHeight2.getText();
                String width = enterWidth.getText();
                String width2 = enterWidth2.getText();
                try{
                    //get the dimensions for the block
                    int blockHeight = Integer.parseInt(height);
                    int blockWidth = Integer.parseInt(width);
                    int blockHeight2 = Integer.parseInt(height2);
                    int blockWidth2 = Integer.parseInt(width2);
                    //create a block with height and width
                    BlockView b = new BlockView(blockHeight, blockWidth, blockHeight2+1, blockWidth2+1,
                            model.getBlockHeight(), model.getBlockHeight(),true);

                    //undo creating a block
                    UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                        //redo the undone method
                        public void redo() throws CannotRedoException {
                            super.redo();
                            model.addBlock(b);
                            model.notifyObsevers();
                        }

                        public void undo() throws CannotUndoException {
                            super.undo();
                            model.deleteBlock(b);
                            model.notifyObsevers();
                        }
                    };
                    //a block is created and added to the list
                    model.addToEdit(undoableEdit);
                    model.addBlock(b);
                    model.notifyObsevers();
                }catch(NumberFormatException err){
                    JOptionPane.showMessageDialog(null,"Please Enter Valid Numbers" );
                }
            }
        });

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Deleting Block");
                BlockView b = model.getSelectedBlock();

                //undo deleting a block
                UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                    //redo the undone method
                    public void redo() throws CannotRedoException {
                        super.redo();
                        model.deleteBlock(model.getSelectedBlock());
                        model.notifyObsevers();
                    }

                    public void undo() throws CannotUndoException {
                        super.undo();
                        model.addBlock(b);
                        model.notifyObsevers();
                    }
                };
                model.addToEdit(undoableEdit);
                model.deleteBlock(model.getSelectedBlock());
                model.notifyObsevers();
            }
        });

        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Saving File");
                JFileChooser saveFile  = new JFileChooser();
                saveFile.setDialogTitle("Choose where to Save");
                int selection = saveFile.showSaveDialog(null);
                if(selection == JFileChooser.APPROVE_OPTION) {
                    try (FileWriter fr = new FileWriter(saveFile.getSelectedFile() + ".txt")) {
                        String width = model.getBlockWidthString();
                        String height = model.getBlockHeightString();
                        fr.write(width + "," + height + "\n");
                        for (BlockView b : model.returnBlocks()) {
                            fr.write(b.getLine() + "\n");
                        }
                        fr.close();
                    }catch(Exception ex) {
                        JOptionPane.showMessageDialog(null, "Unable to Save");
                        ex.printStackTrace();
                    }
                }
            }
        });

    }


    //doesn't really need model data here
    public void update(Object obs) {
        if(obs instanceof LevelEditorModel){
            System.out.println("LevelEditorView - CreateBlock Notified!");
        }
    }

}
