import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.List;

/**
 * Created by Colin on 2017-07-15.
 */

// TODO move the is game paused into the model
//this class is a container that holds the the options the pause menu and the paintable components
public class Animate extends JPanel implements Observer {
    private GameModel model;
    private AnimateComponent paintable;
    private OptionsBar options;

    public Animate(GameModel model, List<BlockView> blocks, int blockHeight, int blockWidth) {
        this.model = model;
        model.addObserver(this);
        model.notifyObservers();

        this.setLayout(new BorderLayout());
        //add the OptionsPanel class
        this.options = new OptionsBar(model);
        this.add(options,BorderLayout.NORTH);
        //add the paintable component
        this.paintable = new AnimateComponent(model, blocks, blockHeight,blockWidth);
        this.add(paintable, BorderLayout.CENTER);

    }


    public void stopTimer(){
        this.paintable.stopTimer();
    }

    @Override
    public void update(Object obs) {
        if(obs instanceof GameModel){
            //redefine the model as a new version may need to be passed
            this.model = ((GameModel) obs);
        }
    }

}

//this class handles the painting
class AnimateComponent extends JPanel implements Observer {
    private GameModel model;
    //animated components
    private List<BlockView> blocks;
    private SpaceShipModel shipModel;
    private SpaceShipView spaceShip;
    //model elements
    private int FPS;
    private int scrollSpeed;
    //game model elements
    private int blockHeight;
    private int blockWidth;
    //pasuing restart and timer portions
    private Timer timer;

    public AnimateComponent(GameModel model, List<BlockView> blockList, int blockHeight, int blockWidth) {
        super();

        this.model = model;
        model.addObserver(this);
        model.notifyObservers();

        //set focus
        this.setFocusable(true);
        this.requestFocusInWindow();

        //create gameplay basics
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
        this.FPS = model.getFPS();
        this.scrollSpeed = model.getScrollSpeed();

        //create ship with model
        this.shipModel = new SpaceShipModel(5,5, this.blockHeight);

        this.spaceShip = createShip();

        //create the blocks with it's model
        this.blocks = blockList;

        //add key listener for the SpaceShip
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int blockSize = getHeight() / blockHeight;
                //always move ship a block at a time
                if(e.getKeyCode() == KeyEvent.VK_UP && !model.getIsGamePaused()|| e.getKeyCode() == KeyEvent.VK_W && !model.getIsGamePaused()){
                    shipModel.updateCurrentPos(0, -1 );
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN && !model.getIsGamePaused() || e.getKeyCode() == KeyEvent.VK_S && !model.getIsGamePaused()){
                    shipModel.updateCurrentPos(0, 1);
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT  && !model.getIsGamePaused() || e.getKeyCode() == KeyEvent.VK_A && !model.getIsGamePaused()){
                    shipModel.updateCurrentPos(-1,0);
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT && !model.getIsGamePaused() || e.getKeyCode() == KeyEvent.VK_D && !model.getIsGamePaused()){
                    shipModel.updateCurrentPos(1,0);
                }
                if(e.getKeyCode() == KeyEvent.VK_R){
                    model.togglePause();
                    JDialog myRestart = new JDialog();

                    JPanel restartContainer = new JPanel();
                    restartContainer.setLayout(new BoxLayout(restartContainer, BoxLayout.Y_AXIS));

                    JPanel textContainer = new JPanel();
                    textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));

                    myRestart.setSize(new Dimension(256,128));
                    myRestart.setTitle("Restart!");

                    JLabel restartText = new JLabel("Exit Window to Restart Game");
                    restartText.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
                    textContainer.add(Box.createHorizontalGlue());
                    textContainer.add(restartText);
                    textContainer.add(Box.createHorizontalGlue());


                    restartContainer.add(textContainer);
                    myRestart.add(restartContainer);

                    //add custom window listener to stop the timer on close
                    myRestart.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            model.togglePause();
                            model.notifyObservers();
                            spaceShip.resetOff();
                            shipModel.resetPostion();
                            for(BlockView b : blocks){
                                b.resetOff();
                            }
                            model.resetScore();
                            model.notifyObservers();
                            super.windowClosed(e);
                        }
                    });

                    myRestart.setVisible(true);
                }
                if(e.getKeyCode() == KeyEvent.VK_P){
                    model.togglePause();
                    model.notifyObservers();
                    JButton okayButton = new JButton("OK");
                    JDialog myPause = new JDialog();

                    JPanel pauseContainer = new JPanel();
                    pauseContainer.setLayout(new BoxLayout(pauseContainer, BoxLayout.Y_AXIS));

                    JPanel textContainer = new JPanel();
                    textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));

                    JPanel buttonContainer = new JPanel();
                    buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));

                    myPause.setSize(new Dimension(256,128));
                    myPause.setTitle("Game Paused!");

                    JLabel pauseText = new JLabel("Press OK to resume game");
                    pauseText.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
                    textContainer.add(Box.createHorizontalGlue());
                    textContainer.add(pauseText);
                    textContainer.add(Box.createHorizontalGlue());

                    buttonContainer.add(Box.createHorizontalGlue());
                    buttonContainer.add(okayButton);
                    buttonContainer.add(Box.createHorizontalGlue());

                    pauseContainer.add(textContainer);
                    pauseContainer.add(Box.createVerticalGlue());
                    pauseContainer.add(buttonContainer);
                    pauseContainer.add(Box.createVerticalGlue());
                    myPause.add(pauseContainer);

                    //add custom window listener to stop the timer on close
                    myPause.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            model.togglePause();
                            model.notifyObservers();
                            super.windowClosed(e);
                        }
                    });

                    okayButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            model.togglePause();
                            model.notifyObservers();
                            myPause.dispose();
                        }
                    });
                    myPause.setVisible(true);
                }
            }
        });

        this.timer =
                //update the bounding box for the player and obstacles to have hit detection
                new Timer(1000 / this.FPS, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!model.getIsGamePaused()) {
                            model.setScore();
                            model.notifyObservers();
                            checkWin();
                            checkCollision();
                            repaint();
                        }
                    }
                });

        this.timer.start();

    }

    public void stopTimer(){
        this.timer.stop();
    }

    private SpaceShipView createShip(){
        int xPos = this.shipModel.getxPos();
        int yPos = this.shipModel.getyPos();
        SpaceShipView ship = new SpaceShipView(xPos, yPos, this.blockHeight, this.blockWidth);
        return ship;
    }

    @Override
    protected void paintComponent(Graphics g){
        //set up the scroll values
        float pushBackPercentage = (((float) this.scrollSpeed/ (float) this.FPS) / this.blockWidth);

        //get block height and width and push back value
        int prefHeight = this.getHeight()/this.blockHeight;
        int prefWidth = ((prefHeight * this.spaceShip.getImageWidth()) / this.spaceShip.getImageHeight());

        //scale the ship to the height
        this.shipModel.setNewSize(this.getHeight());
        this.shipModel.recalculateBlocks();

        //get the vars for repainting the ship
        int x = this.shipModel.getxPos();
        int y = this.shipModel.getyPos();


        //set the new values to the view
        this.spaceShip.setOffSet(pushBackPercentage);

        this.spaceShip.setNewHeight(prefHeight);
        this.spaceShip.setNewWidth(prefWidth);
        this.spaceShip.setNewX(x);
        this.spaceShip.setNewY(y);
        //paint the ship
        this.spaceShip.paint(g);


        //print all the blocks
        for (BlockView b : this.blocks){
            b.setNewSize(this.getHeight());
            b.setOffset(pushBackPercentage);
            b.recalculateBlocks();

            b.paint(g);
        }
    }

    private void checkWin(){
        int blockSize = getHeight() / model.getStartHeight();
        int levelWidth = blockSize * model.getStartWidth();
        if (shipModel.getxPos() >= levelWidth){
            model.togglePause();
            model.notifyObservers();
            JDialog winner = new JDialog();

            JPanel winnerContainer = new JPanel();
            winnerContainer.setLayout(new BoxLayout(winnerContainer, BoxLayout.Y_AXIS));

            JPanel textContainer = new JPanel();
            textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));

            winner.setSize(new Dimension(256,128));
            winner.setTitle("Game Over!");

            JLabel pauseText = new JLabel("You Won! You're a Great Player!");
            pauseText.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
            textContainer.add(Box.createHorizontalGlue());
            textContainer.add(pauseText);
            textContainer.add(Box.createHorizontalGlue());

            winnerContainer.add(textContainer);
            winnerContainer.add(Box.createVerticalGlue());
            winner.add(winnerContainer);

            //add custom window listener to stop the timer on close
            winner.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    model.setGameOver();
                    model.notifyObservers();
                    super.windowClosed(e);
                }
            });

            winner.setVisible(true);
        }
    }


    private void checkCollision(){
        //create the rectangle
        Rectangle p = new Rectangle(shipModel.getxPos(), shipModel.getyPos(),
                spaceShip.getCurrentWidth(), spaceShip.getCurrentHeight());
        int blockSize = getHeight() / model.getStartHeight();
        int levelWidth = blockSize * model.getStartWidth();

        //check if the box collides with any blocks in the array
        for (BlockView b : this.blocks){
            if (p.intersects(b.createBoundingBox()) || shipModel.getyPos() < 0
                    || shipModel.getxPos() < spaceShip.getOffset() * levelWidth ){
                model.togglePause();
                model.notifyObservers();
                JButton okayButton = new JButton("Quit Game");
                JDialog gameOver = new JDialog();

                JPanel gameOverContainer = new JPanel();
                gameOverContainer.setLayout(new BoxLayout(gameOverContainer, BoxLayout.Y_AXIS));

                JPanel textContainer = new JPanel();
                textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));

                JPanel buttonContainer = new JPanel();
                buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.X_AXIS));

                gameOver.setSize(new Dimension(256,128));
                gameOver.setTitle("Game Over!");

                JLabel pauseText = new JLabel("Game Over...Please Play Again");
                pauseText.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
                textContainer.add(Box.createHorizontalGlue());
                textContainer.add(pauseText);
                textContainer.add(Box.createHorizontalGlue());

                buttonContainer.add(Box.createHorizontalGlue());
                buttonContainer.add(okayButton);
                buttonContainer.add(Box.createHorizontalGlue());

                gameOverContainer.add(textContainer);
                gameOverContainer.add(Box.createVerticalGlue());
                gameOverContainer.add(buttonContainer);
                gameOverContainer.add(Box.createVerticalGlue());
                gameOver.add(gameOverContainer);

                //add custom window listener to stop the timer on close
                gameOver.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        model.setGameOver();
                        model.notifyObservers();
                        super.windowClosed(e);
                    }
                });

                okayButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameOver.dispose();
                        model.setGameOver();
                        model.notifyObservers();
                    }
                });
                gameOver.setVisible(true);
                break;
            }
        }
    }


    @Override
    public void update(Object obs) {
        if(obs instanceof GameModel){
            //redefine the model as a new version may need to be passed
            this.model = ((GameModel) obs);
        }
    }


}

//this will be the pause menu that appears when the player pauses the game
class OptionsBar extends JComponent implements Observer{
    private GameModel model;
    private JLabel score;
    private int gameScore;

    public OptionsBar(GameModel model){
        this.model = model;
        model.addObserver(this);
        model.notifyObservers();

        //component options
        this.setLayout(new GridLayout(1,3));
        this.setBackground(Color.DARK_GRAY);

        //pause option
        JLabel pause = new JLabel("Pause: P");
        pause.setFont(new Font("SANS_SERIF", Font.PLAIN,15));
        pause.setOpaque(true);
        this.add(pause);

        //Restart option
        JLabel restart = new JLabel("Restart: R");
        restart.setFont(new Font("SANS_SERIF", Font.PLAIN,15));
        restart.setOpaque(true);
        this.add(restart);

        //Score option
        this.score = new JLabel("Score: " + model.getScore());
        score.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        this.add(score);
    }

    public void updateScore() {
        this.score.setText("Score: " + this.gameScore);
    }

    @Override
    public void update(Object obs) {
        if(obs instanceof GameModel){
            this.model = ((GameModel) obs);
            if(model.getScore() > this.gameScore || model.getScore() == 100){
                this.gameScore = model.getScore();
                updateScore();
            }
        }
    }
}
