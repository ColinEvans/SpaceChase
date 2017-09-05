import javax.swing.*;
import java.util.*;
import java.io.*;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private JPanel mainPage;
    private int FPS = 30;
    private int scrollSpeed = 1;
    private File level;

    /**
     * Create a new model.
     */
    public Model() {
        this.observers = new ArrayList();
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    //getters
    public int getFPS() {
        return this.FPS;
    }

    public int getScrollSpeed() {
        return this.scrollSpeed;
    }


    //setters
    public void setFPS(int FPS){
        this.FPS = FPS;
    }

    public void setScrollSpeed(int scrollSpeed){
        this.scrollSpeed = scrollSpeed;
    }

    public void setLevel(File file){
        this.level = file;
    }

    public File getLevel(){
        return this.level;
    }

    public void setMainPage(JPanel mainPage){
        this.mainPage = mainPage;
    }

    public JPanel getMainPage(){
        return this.mainPage;
    }
}
