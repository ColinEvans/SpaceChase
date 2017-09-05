/**
 * Created by Colin on 2017-07-20.
 */

import javax.swing.undo.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.io.File;

public class LevelEditorModel implements ClipboardOwner{
    private int blockHeight;
    private int blockWidth;
    private int blockSize;
    private List<BlockView> blocks;
    private List<Observer> views;
    private BlockView selectedBlock;
    private UndoManager undoManager;


    //for loading in a level
    private BufferedReader buff;
    private File loadLevel;
    private String[] screenSize;
    private String[] blockSizes;

    LevelEditorModel(int prefHieght, int prefWidth, File loadLevel){
        this.undoManager = new UndoManager();
        this.blockHeight = prefHieght;
        this.blockWidth = prefWidth;
        this.blocks = new ArrayList();
        this.views = new ArrayList();

        this.loadLevel = loadLevel;
        readFile();
    }

    public void addBlock(BlockView b){
        this.blocks.add(b);
    }

    //deletes the passed block from the list
    public void deleteBlock(BlockView b){
        this.blocks.remove(b);
    }

    public List<BlockView> returnBlocks(){
        return this.blocks;
    }

    public int getBlockHeight(){
        return this.blockHeight;
    }

    public int getBlockWidth() {
        return blockWidth;
    }

    public void addObserver(Observer v){
        this.views.add(v);
    }

    public void removeObserver(Observer v){
        this.views.remove(v);
    }

    public void notifyObsevers() {
        for (Observer view: this.views) {
            view.update(this);
        }
    }

    public void setSelectedBlock(BlockView b) {
        int index = this.blocks.indexOf(b);
        this.selectedBlock = this.blocks.get(index);
    }

    public BlockView getSelectedBlock(){
        return this.selectedBlock;
    }

    public void removeSelectedBlock(){
        this.selectedBlock = null;
    }

    public String getBlockWidthString(){
        String w = Integer.toString(this.blockWidth);
        return w;
    }

    public String getBlockHeightString(){
        String h = Integer.toString(this.blockHeight);
        return h;
    }

    public int getBlockSize(){
        return this.blockSize;
    }

    public void resetBlockSize(int height){
        this.blockSize = height/this.blockHeight;
    }

    private void readFile() {
        try {
            String currentLine;
            buff = new BufferedReader(new FileReader(this.loadLevel));

            //parse the file for comments and screenSize
            while ((currentLine = buff.readLine()) != null ){
                if (currentLine.charAt(0) == '#') {
                    System.out.println(currentLine);
                }else if(currentLine.charAt(0) != '#') {
                    this.screenSize = currentLine.split(",");
                    this.blockWidth= Integer.parseInt(this.screenSize[0]);
                    this.blockHeight = Integer.parseInt(this.screenSize[1]);
                    break;
                }
            }

            //these are the actual blocks that need to be created
            while ((currentLine = buff.readLine()) != null){
                blockSizes = currentLine.split(",");
                //coded based on 4 values accepted
                int x1 = Integer.parseInt(blockSizes[0]);
                int y1 = Integer.parseInt(blockSizes[1]);
                int x2 = Integer.parseInt(blockSizes[2]);
                int y2 = Integer.parseInt(blockSizes[3]);

                //add the block to the list
                BlockView newBlock = new BlockView(x1, y1, x2, y2,this.blockHeight,this.blockWidth,true);
                this.blocks.add(newBlock);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //methods for undoing and redoing
    public void undo() {
        if(canUndo()){
            undoManager.undo();
        }
    }

    public void redo() {
        if(canRedo()){
            undoManager.redo();
        }
    }

    public boolean canUndo(){
        return undoManager.canUndo();
    }

    public boolean canRedo(){
        return undoManager.canRedo();
    }

    public void addToEdit(UndoableEdit edit){
        undoManager.addEdit(edit);
    }

    public void doCopy(){
        Clipboard editorClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();


        Transferable transfer = new Transferable() {
          String s = getSelectedBlock().getLine();

          public Object getTransferData(DataFlavor fv) throws UnsupportedFlavorException, IOException {
              if(fv.equals(DataFlavor.stringFlavor)) {
                  return s;
              }
              throw new UnsupportedFlavorException(fv);
          }

          public DataFlavor[] getTransferDataFlavors(){
              return new DataFlavor[] {DataFlavor.stringFlavor} ;
          }

          public boolean isDataFlavorSupported(DataFlavor fv) {
              return fv.equals(DataFlavor.stringFlavor);
          }

        };

        editorClipboard.setContents(transfer, this);

    }


    public void doCut() {
        doCopy();
        deleteBlock(getSelectedBlock());
    }

    public void doPaste() {
        Clipboard editorClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        if(editorClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try{
                String newBlock = (String) editorClipboard.getData(DataFlavor.stringFlavor);
                String[] blockSizes = newBlock.split(",");
                //coded based on 4 values accepted
                int x1 = Integer.parseInt(blockSizes[0]);
                int y1 = Integer.parseInt(blockSizes[1]);
                int x2 = Integer.parseInt(blockSizes[2]);
                int y2 = Integer.parseInt(blockSizes[3]);

                //add the block to the list
                BlockView b = new BlockView(x1, y1, x2, y2, getBlockHeight(),getBlockWidth(),true);
                addBlock(b);
            } catch (UnsupportedFlavorException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        System.out.println("Lost ownership!");
    }

}
