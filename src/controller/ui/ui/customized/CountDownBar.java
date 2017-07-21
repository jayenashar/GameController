package controller.ui.ui.customized;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Arc2D;

/**
 * Created by rkessler on 2017-06-18.
 */
public class CountDownBar extends JPanel {

    private int dx = 50;
    private int dy = 50;
    private double percent = 0;

    public CountDownBar() {
        super();
        this.repaint();
        this.setOpaque(false);
        this.setVisible(false);

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

        if (this.isVisible()) {
            g.setColor(new Color(255, 91, 88));
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.fillRect(0, 0, (int) (dx * (100 - percent) * 0.01), dy);
        }
    }

    public void updateValue(double percent) {
        this.percent = percent;
        this.setVisible(percent > 0);
        this.repaint();
    }
}
