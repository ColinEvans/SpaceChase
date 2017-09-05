import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;

/**
 * Created by Colin on 2017-07-15.
 */
public class BlockView extends JComponent {
    //want to be able to drag and drop my box components
    private boolean isInEditMode;
    private boolean isSelected;

    //offset
    private float offset;

    //recalculating the blocks
    private int topLeftX;
    private int topLeftY;
    private int bottomRightX;
    private int bottomRightY;

    //setting the blockSize
    private int blockSize;
    private int blockHeight;
    private int blockWidth;

    //original sizes (don't change)
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    public BlockView(int x1, int y1, int x2, int y2, int prefHeight, int prefWidth, boolean isInEditMode){
        //set transfer properties if in edit mode
        this.isInEditMode = isInEditMode;
        this.isSelected = false;

        this.blockHeight = prefHeight;
        this.blockWidth = prefWidth;

        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;

    }

    public void recalculateBlocks(){
        this.topLeftY = this.y1 * this.blockSize;
        this.topLeftX = this.x1 * this.blockSize;
        this.bottomRightY = this.y2 * this.blockSize;
        this.bottomRightX = this.x2 * this.blockSize;
    }


    @Override
    public void paint(Graphics g) {
        int levelWidth = this.blockSize * this.blockWidth;
        int newOffset = (int) (this.offset * levelWidth);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        if(this.isSelected) g2.setColor(Color.darkGray);
        g2.fillRect ( this.topLeftX - newOffset, this.topLeftY, (this.bottomRightX - this.topLeftX), (this.bottomRightY - this.topLeftY));
    }

    public void setNewSize(int height){
        this.blockSize = height/this.blockHeight;
    }


    public Rectangle createBoundingBox(){
        return new Rectangle(this.topLeftX, this.topLeftY, (this.bottomRightX - this.topLeftX), (this.bottomRightY - this.topLeftY));
    }

    public int getX(){
        return this.topLeftX;
    }
    public int getY(){
        return this.topLeftY;
    }

    public void setNetX1(int x) {
        this.x1 = x;
    }
    public void setNetY1(int y) {
        this.y1 = y;
    }
    public void setNetX2(int x) {
        this.x2 = x;
    }
    public void setNetY2(int y) {
        this.y2 = y;
    }

    public int getx1(){
        return this.x1;
    }
    public int getx2(){
        return this.x2;
    }
    public int gety1(){
        return this.y1;
    }
    public int gety2(){
        return this.y2;
    }



    public void setOffset(float off){
        this.offset += off;
    }

    public void toggleSelected(){
        this.isSelected = !isSelected;
    }

    public boolean getSelected(){
        return this.isSelected;
    }

    public void resetOff(){
        this.offset = 0;
    }

    public String getLine(){
        String x1 = Integer.toString(this.x1);
        String x2 = Integer.toString(this.x2);
        String y1 = Integer.toString(this.y1);
        String y2 = Integer.toString(this.y2);
        return x1 + "," + y1 + "," + x2 + "," + y2;
    }

}
