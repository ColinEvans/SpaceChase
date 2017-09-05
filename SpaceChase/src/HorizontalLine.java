import javax.swing.*;
import java.awt.*;

/**
 * Created by Colin on 2017-06-30.
 */

public class HorizontalLine extends JPanel {

    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = this.getSize();
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.black);
        g2.drawLine(0, d.height / 2, d.width*(3/2), d.height/2);
    }

    HorizontalLine(){
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

}
