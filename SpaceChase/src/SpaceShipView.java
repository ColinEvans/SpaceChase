import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Colin on 2017-07-15.
 */

public class SpaceShipView extends JComponent {
    //
    private float offset = 0;

    // make the shape of the spaceship
    private BufferedImage ship;

    //current location of the ship
    private int x, y;

    //get actual image height
    private int imageHeight;
    private int imageWidth;

    //ship's height and width based on the size of the screen
    private int blockHeight;
    private int blockWidth;
    private int currentHeight;
    private int currentWidth;


    public SpaceShipView(int x, int y, int prefHeight, int prefWidth) {
        this.blockHeight = prefHeight;
        this.blockWidth = prefWidth;
        this.x = x;
        this.y = y;
        this.createImage();

    }

    //creates the actual shape
    private void createImage() {
        try {
            this.ship = ImageIO.read(new File("gameShip.png"));
            this.imageHeight = ship.getHeight();
            this.imageWidth = ship.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setNewHeight(int height) {
        this.currentHeight = height;
    }

    public void setNewWidth(int width){
        this.currentWidth = width;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public void setNewX(int x){
        this.x = x;
    }

    public void setNewY(int y){
        this.y = y;
    }

    public int getImageWidth(){
        return this.imageWidth;
    }

    public int getImageHeight(){
        return this.imageHeight;
    }

    public void paint(Graphics g){
        int levelWidth = this.currentHeight * this.blockWidth;
        int newOffset = (int) (this.offset * levelWidth);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.ship, this.x - newOffset, this.y, this.currentWidth, this.currentHeight, null);

    }

    public void setOffSet(float off){
        this.offset += off;
    }

    public float getOffset() {return this.offset;}

    public void resetOff(){
        this.offset = 0;
    }

}
