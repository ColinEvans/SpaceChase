import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by Colin on 2017-07-24.
 */
class myFileChooser extends JPanel implements ActionListener {
    private JButton open;
    private JLabel selectText;
    private JFileChooser fileChooser;
    private File file;

    public myFileChooser(){
        super(new BorderLayout());
        this.setFocusable(true);
        this.requestFocusInWindow();

        JPanel gridContainer = new JPanel();
        gridContainer.setLayout(new GridLayout(2,1));

        //set fileChooser
        fileChooser = new JFileChooser();
        FileNameExtensionFilter txt = new FileNameExtensionFilter(".txt only","txt");
        fileChooser.setFileFilter(txt);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        //set the JLabel
        selectText = new JLabel("Only .txt Files supported");
        selectText.setHorizontalTextPosition(JLabel.CENTER);
        selectText.setFont(new Font("SANS_SERIF", Font.PLAIN,40));
        selectText.setOpaque(true);

        //set the Button
        open = new JButton("Open File");
        open.setFont(new Font("SANS_SERIF", Font.PLAIN,50));
        open.addActionListener(this);

        gridContainer.add(selectText, BorderLayout.CENTER);
        gridContainer.add(open);

        this.add(gridContainer);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == open){
            this.changeSelect();
            int returnValue = fileChooser.showOpenDialog(myFileChooser.this);
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                this.file = fileChooser.getSelectedFile();
            }
        }
    }

    public File getFile() {
        if (this.file != null) {
            return this.file;
        }else{
            System.out.println("no file selected No new file returned returned");
            this.file = null;
            return this.file;
        }
    }

    public void changeSelect(){
        this.selectText.setText("File Loaded!");
    }

}

