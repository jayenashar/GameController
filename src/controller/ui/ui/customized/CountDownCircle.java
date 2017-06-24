package controller.ui.ui.customized;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Arc2D;

/**
 * Created by rkessler on 2017-06-18.
 */
public class CountDownCircle extends JPanel {

    private int dx = 50;
    private int dy = 50;
    private int secondsRemaining = 0;
    private double percent = 0;

    public CountDownCircle() {
        super();
        this.repaint();
        this.setOpaque(false);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                dx = (int) e.getComponent().getSize().getWidth();
                dy = (int) e.getComponent().getSize().getHeight();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = (int) (percent * 3.6);

        for (int i=2; i < 10; i++){
            g2.draw(new Arc2D.Double(i, i, dx-2*i, dy-2*i, 90, arc, Arc2D.OPEN));
        }


        g.setColor(Color.black);
        int size = g.getFont().getSize();
        g.drawString(String.valueOf(this.secondsRemaining), (dx / 2) - (int) ((size / 2.0)), (dy / 2) + (int) ((size / 2.0)));


    }

    public void updateValue(int secondsRemaining, double percent) {
        this.secondsRemaining = secondsRemaining;
        this.percent = percent;
        this.setVisible(percent > 0);
        this.repaint();
    }
}
