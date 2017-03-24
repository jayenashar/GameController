package controller.ui.gameplay;

import data.states.AdvancedData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rkessler on 2017-03-23.
 */
public class DebugUI extends JFrame {

    private AdvancedData data;
    private JTextArea ta;

    public DebugUI(AdvancedData data){
        this.data = data;
        setSize(400, 600);
        setResizable(true);
        setVisible(true);
        setLayout(new FlowLayout());

        JButton b = new JButton("Refresh");
        b.setSize(400, 100);
        ta = new JTextArea();
        ta.setSize(400, 100);

        this.add(b);
        this.add(ta);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateAdvanceData();
            }
        });
    }

    public void updateAdvanceData(){
        ta.setText("");

        String text = new String();

        text += "FreeKickActive:" + this.data.freeKickActive[0] + " // " + this.data.freeKickActive[1];

        ta.setText(text);

    }
}
