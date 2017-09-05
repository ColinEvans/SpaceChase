import javax.swing.*;
import java.awt.*;

/**
 * Created by Colin on 2017-07-22.
 */
public class HelpMenu extends JFrame {

    public HelpMenu(){
        // Basic Frame Options
        this.setTitle("Space Chase");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(1200, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //all the panels required
        JPanel frameContainer = new JPanel();
        frameContainer.setLayout(new GridLayout(4,1));

        //title area
        JPanel welcomeText = new JPanel();
        welcomeText.setLayout(new GridLayout(2,1));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(1,3));
        JPanel lineBreak = new JPanel();
        lineBreak.setLayout(new GridLayout(1,3));

        //controls area
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2,1));
        JPanel controlTitlePanel = new JPanel();
        controlTitlePanel.setLayout(new GridLayout(1,2));

        JPanel controlsArea = new JPanel();
        controlsArea.setLayout(new GridLayout(4,1));
        JPanel controlOne = new JPanel();
        controlOne.setLayout(new GridLayout(1,4));
        JPanel controlTwo = new JPanel();
        controlTwo.setLayout(new GridLayout(1,4));
        JPanel controlThree = new JPanel();
        controlThree.setLayout(new GridLayout(1,4));
        JPanel controlFour = new JPanel();
        controlFour.setLayout(new GridLayout(1,4));

        //Enhancement Area
        JPanel enhancePanel = new JPanel();
        enhancePanel.setLayout(new GridLayout(2,1));
        JPanel enhanceTitlePanel = new JPanel();
        enhanceTitlePanel.setLayout(new GridLayout(1,2));

        //all the components to the frame all read only
        JLabel title = new JLabel("Welcome to Space Chase!");
        title.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        //create the line break
        HorizontalLine textBreak = new HorizontalLine();
        lineBreak.add(Box.createHorizontalGlue());
        lineBreak.add(textBreak);
        lineBreak.add(Box.createHorizontalGlue());

        //first part to the frame
        welcomeText.add(titlePanel);
        welcomeText.add(lineBreak);

        //Controls Title
        JLabel controlTitle = new JLabel("Controls: ");
        controlTitle.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        controlTitlePanel.add(controlTitle);
        controlTitlePanel.add(Box.createHorizontalGlue());

        //Control labels
        JLabel up = new JLabel("    UP:     W/Up Arrow");
        up.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
        JLabel down = new JLabel("    DOWN:   S/Down Arrow");
        down.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
        JLabel left = new JLabel("    LEFT:     A/Left Arrow");
        left.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
        JLabel right = new JLabel("    RIGHT:   D/Right Arrow");
        right.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
        JLabel pause = new JLabel("Pause Game:      P");
        pause.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));
        JLabel restart = new JLabel("Restart Level:     R");
        restart.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));

        //Add controls to correct panel
        controlOne.add(up);
        controlOne.add(Box.createHorizontalGlue());
        controlOne.add(pause);

        controlTwo.add(down);
        controlTwo.add(Box.createHorizontalGlue());
        controlTwo.add(restart);

        controlThree.add(left);
        controlThree.add(Box.createHorizontalGlue());
        controlThree.add(new JPanel());

        controlFour.add(right);
        controlFour.add(Box.createHorizontalGlue());
        controlFour.add(new JPanel());

        controlsArea.add(controlOne);
        controlsArea.add(controlTwo);
        controlsArea.add(controlThree);
        controlsArea.add(controlFour);

        //add to top level control container
        controlPanel.add(controlTitlePanel);
        controlPanel.add(controlsArea);

        //add the Enhancements area
        JLabel enhanceTitle = new JLabel("Enhancements: ");
        enhanceTitle.setFont(new Font("SANS_SERIF", Font.PLAIN, 30));
        enhanceTitlePanel.add(enhanceTitle);
        enhanceTitlePanel.add(Box.createHorizontalGlue());

        //create the list of enhancements
        JLabel enhanceList = new JLabel("<html><ul>" +
                "<li>Created Ship With an Image</li>" +
                "<li>Game and Level Editor implemented in separate frames</li>" +
                "</ul><html>");
        enhanceList.setFont(new Font("SANS_SERIF", Font.PLAIN, 20));

        //add to parent enhance panel
        enhancePanel.add(enhanceTitlePanel);
        enhancePanel.add(enhanceList);

        //add all the parts to the parent Panel
        frameContainer.add(welcomeText);
        frameContainer.add(controlPanel);
        frameContainer.add(enhancePanel);

        //add to the frame and set visible to true
        this.add(frameContainer);
        this.setVisible(true);
    }
}
