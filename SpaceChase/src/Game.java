/**
 * Created by Colin on 2017-06-17.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Game extends JFrame implements Observer{
    private GameModel gameModel;
    private int startWidth;
    private int startHeight;

    public Game(Model model) {
        //set up the in game model
        this.gameModel = new GameModel(model.getLevel(),model.getFPS(),model.getScrollSpeed());
        this.gameModel.addObserver(this);
        this.gameModel.notifyObservers();
        this.startWidth = gameModel.getStartWidth();
        this.startHeight = gameModel.getStartHeight();

        Animate gameAnimation = new Animate(gameModel, this.gameModel.returnBlocks(), this.startHeight, this.startWidth);

        this.setTitle("Space Chase - In Game");
        this.setContentPane(gameAnimation);
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        setVisible(true);

        //add custom window listener to stop the timer on close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                gameAnimation.stopTimer();
                super.windowClosed(e);
            }
        });
    }

    public void update(Object obs) {
        if(obs instanceof GameModel){
            this.gameModel = ((GameModel) obs);
            if (gameModel.getGameOver()) super.dispose();
        }
    }

}