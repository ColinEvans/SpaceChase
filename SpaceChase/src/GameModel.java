/**
 * Created by Colin on 2017-07-16.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class GameModel {
    private boolean gameOver = false;
    private List<BlockView> blocks;
    private List<Observer> observers;
    private int score;
    private int FPS;
    private int scrollSpeed;
    private BufferedReader buff;
    File defaultLevel = new File("sample_level.txt");
    private File level;
    private String[] screenSize;
    private String[] blockSize;
    private int startWidth;
    private int startHeight;
    private int scoreValue = 100;
    private boolean isGamePaused = false;

    GameModel(File level, int FPS, int scrollSpeed ){
        this.FPS = FPS;
        this.scrollSpeed = scrollSpeed;
        this.level = level;
        this.blocks = new ArrayList();
        this.observers = new ArrayList();
        this.score = 0;
        readFile();
    }

    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    private void readFile() {
        try {
            String currentLine;
            if(this.level != null) buff = new BufferedReader(new FileReader(level));
            if(this.level == null) buff = new BufferedReader(new FileReader(defaultLevel));

            //parse the file for comments and screenSize
            while ((currentLine = buff.readLine()) != null ){
                if (currentLine.charAt(0) == '#') {
                    System.out.println(currentLine);
                }else if(currentLine.charAt(0) != '#') {
                    this.screenSize = currentLine.split(",");
                    this.startWidth = Integer.parseInt(this.screenSize[0]);
                    this.startHeight = Integer.parseInt(this.screenSize[1]);
                    break;
                }
            }

            //these are the actual blocks that need to be created
            while ((currentLine = buff.readLine()) != null){
                blockSize = currentLine.split(",");
                //coded based on 4 values accepted
                int x1 = Integer.parseInt(blockSize[0]);
                int y1 = Integer.parseInt(blockSize[1]);
                int x2 = Integer.parseInt(blockSize[2]);
                int y2 = Integer.parseInt(blockSize[3]);

                //add the block to the list
                BlockView newBlock = new BlockView(x1, y1, x2, y2,this.startHeight,this.startWidth,false);
                this.blocks.add(newBlock);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getStartWidth() {
        return this.startWidth;
    }

    public  int getStartHeight() { return this.startHeight; }

    public List<BlockView> returnBlocks() {
        return this.blocks;
    }

    public void setScore() {
        this.score += scoreValue;
    }

    public void resetScore() {this.score = 100; }

    public int getScore() {
        return this.score;
    }

    public int getFPS() {
        return this.FPS;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
    }

    public void setGameOver(){ this.gameOver = true; }

    public boolean getGameOver(){ return this.gameOver; }

    public void togglePause(){
        this.isGamePaused = !isGamePaused;
    }

    public boolean getIsGamePaused(){
        return this.isGamePaused;
    }

}
