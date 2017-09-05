/**
 * Created by Colin on 2017-07-15.
 */
public class SpaceShipModel {
    private int xPos;
    private int yPos;

    //scale the position based on blockHeight set
    private int x;
    private int y;
    private int blockSize;
    private int blockHeight;

    public SpaceShipModel(int xPos, int yPos, int prefHeight){
        this.blockHeight = prefHeight;
        this.x = xPos;
        this.y = yPos;


    }


    public void updateCurrentPos(int newXPos, int newYPos) {
        this.x += newXPos;
        this.y += newYPos;
    }

    public void resetPostion(){
        this.x = 5;
        this.y = 5;
    }


    public int getxPos(){
        return this.xPos;
    }

    public int getyPos(){
        return this.yPos;
    }


    //scale position based on movement
    public void setNewSize(int height){
        this.blockSize = height / this.blockHeight;
    }

    public void recalculateBlocks(){
        this.xPos = this.blockSize * this.x;
        this.yPos = this.blockSize * this.y;
    }


}
